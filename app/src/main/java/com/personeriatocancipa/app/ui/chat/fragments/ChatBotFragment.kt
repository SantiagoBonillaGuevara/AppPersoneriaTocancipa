package com.personeriatocancipa.app.ui.chat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentChatbotBinding

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

        binding.btnCreatePqrs.setOnClickListener {
            findNavController().navigate(R.id.action_chat_to_createPqrs)
        }
        binding.btnListPqrs.setOnClickListener {
            findNavController().navigate(R.id.action_chat_to_listPqrs)
        }
        binding.btnSendMessage.setOnClickListener {
            val text = binding.etMessageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                // TODO: enviar mensaje al bot y actualizar RecyclerView
                binding.etMessageInput.text?.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

