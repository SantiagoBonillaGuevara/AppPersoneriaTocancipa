package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsCreateBinding

class PqrsCreateFragment : Fragment() {
    private var _binding: FragmentPqrsCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPqrsCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val types = listOf("Petición","Queja","Reclamo","Sugerencia")
        binding.spinnerType.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)

        binding.btnAttach.setOnClickListener {
            // TODO: abrir selector de archivos
        }
        binding.btnSend.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val desc  = binding.etDescription.text.toString().trim()
            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: llamar CreatePqrsUseCase
            findNavController().navigate(R.id.action_create_to_list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
