package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminDetailBinding

class PqrsAdminDetailFragment : Fragment() {
    private var _binding: FragmentPqrsAdminDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PqrsAdminDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentPqrsAdminDetailBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: cargar datos y poblar fields
        binding.btnSendResponse.setOnClickListener {
            val resp = binding.etResponse.text.toString().trim()
            if (resp.isNotEmpty()) {
                // TODO: RespondPqrsUseCase
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
