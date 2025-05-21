package com.personeriatocancipa.app.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentChatbotBinding

class ChatBotFragment : Fragment() {

    private var _b: FragmentChatbotBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentChatbotBinding.inflate(inflater, container, false).also { _b = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // tu lógica actual…

        b.btnGoHome.setOnClickListener {
            findNavController().navigate(R.id.action_chatBotFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
