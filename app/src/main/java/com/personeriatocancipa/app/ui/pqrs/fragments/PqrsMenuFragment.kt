package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsMenuBinding

class PqrsMenuFragment : Fragment() {
    private var _binding: FragmentPqrsMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPqrsMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.pqrsCreateFragment)
        }
        binding.btnList.setOnClickListener {
            findNavController().navigate(R.id.pqrsListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
