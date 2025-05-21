package com.personeriatocancipa.app.ui.chat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentChatbotBinding
import kotlinx.coroutines.launch

class ChatBotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NAVEGACIÓN A CREAR PQRS
        binding.btnCreatePqrs.setOnClickListener {
            findNavController().navigate(
                R.id.action_chatBotFragment_to_pqrsCreateFragment
            )
        }

        // NAVEGACIÓN A LISTAR PQRS
        binding.btnListPqrs.setOnClickListener {
            findNavController().navigate(
                R.id.action_chatBotFragment_to_pqrsListFragment
            )
        }

        // ENVÍO DE MENSAJE AL BOT
        binding.btnSendMessage.setOnClickListener {
            val text = binding.etMessageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                // Aquí implementas la lógica real de tu bot.
                // Por ejemplo, si tienes un ViewModel:
                // lifecycleScope.launch { chatBotViewModel.sendMessage(text) }
                // y luego actualizas el RecyclerView observando LiveData/Flow.

                // Por ahora limpia el campo:
                binding.etMessageInput.text?.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
