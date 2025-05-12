package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personeriatocancipa.app.databinding.FragmentPqrsDetailBinding

class PqrsDetailFragment : Fragment() {
    private var _binding: FragmentPqrsDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PqrsDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentPqrsDetailBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pqrsId = args.pqrsId
        // TODO: cargar datos con GetPqrsByIdUseCase
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
