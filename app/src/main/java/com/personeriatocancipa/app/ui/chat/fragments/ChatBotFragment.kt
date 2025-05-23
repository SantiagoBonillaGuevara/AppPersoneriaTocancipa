// app/src/main/java/com/personeriatocancipa/app/ui/chat/fragments/ChatBotFragment.kt
package com.personeriatocancipa.app.ui.chat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.BuildConfig
import com.personeriatocancipa.app.databinding.FragmentChatbotBinding
import com.personeriatocancipa.app.domain.usecase.SendMessageUseCase
import com.personeriatocancipa.app.ui.chat.ChatBotAdapter
import com.personeriatocancipa.app.ui.chat.viewmodel.ChatBotViewModel
import kotlinx.coroutines.flow.collectLatest

class ChatBotFragment : Fragment() {

    private var _b: FragmentChatbotBinding? = null
    private val b get() = _b!!

    // 1) Crea tu ViewModel pasando la API key
    private val viewModel: ChatBotViewModel by viewModels {
        ChatBotViewModel.Factory(
            SendMessageUseCase(BuildConfig.GEMINI_API_KEY)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentChatbotBinding.inflate(inflater, container, false).also { _b = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2) Instancia y configura el adapter
        val adapter = ChatBotAdapter()
        b.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        b.rvMessages.adapter = adapter

        // 3) Observa la conversación y actualiza el adapter
        lifecycleScope.launchWhenStarted {
            viewModel.conversation.collectLatest { list ->
                adapter.submitList(list)
                // auto-scroll al último mensaje
                if (list.isNotEmpty()) {
                    b.rvMessages.scrollToPosition(list.size - 1)
                }
            }
        }
        b.btnFaq1.setOnClickListener { viewModel.sendMessage("¿Qué es una PQRS?") }
        b.btnFaq2.setOnClickListener { viewModel.sendMessage("¿Cómo envío una PQRS?") }
        b.btnFaq3.setOnClickListener { viewModel.sendMessage("¿Cuánto tarda la respuesta?") }


        // 4) Envía mensaje al pulsar enviar
        b.btnSendMessage.setOnClickListener {
            val text = b.etMessageInput.text.toString().trim()
            b.etMessageInput.text?.clear()
            viewModel.sendMessage(text)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
