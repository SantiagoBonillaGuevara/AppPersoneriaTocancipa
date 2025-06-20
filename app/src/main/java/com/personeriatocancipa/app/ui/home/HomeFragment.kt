package com.personeriatocancipa.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2) Navegar al ChatBot dentro de tu NavGraph nav_pqrs.xml
        binding.btnChatBot.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_chatBotFragment)
        }

        // 3) Navegar al módulo PQRS dentro de tu NavGraph
        binding.btnPqrs.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pqrsMenuFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
