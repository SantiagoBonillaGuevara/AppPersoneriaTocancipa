package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.personeriatocancipa.app.databinding.FragmentPqrsListBinding

class PqrsListFragment : Fragment() {

    private var _binding: FragmentPqrsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPqrsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: configurar Adapter y observar datos
        // binding.rvPqrs.adapter = …
        // binding.swipeRefresh.setOnRefreshListener { … }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
