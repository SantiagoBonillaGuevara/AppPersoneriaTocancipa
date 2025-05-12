package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminListBinding

class PqrsAdminListFragment : Fragment() {
    private var _binding: FragmentPqrsAdminListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentPqrsAdminListBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: GetAllPqrsUseCase + adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
