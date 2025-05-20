package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminListBinding
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.domain.usecase.GetAllPqrsUseCase
import com.personeriatocancipa.app.ui.pqrs.adapter.PqrsAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class PqrsAdminListFragment : Fragment() {
    private var _b: FragmentPqrsAdminListBinding? = null
    private val b get() = _b!!
    private val allUseCase = GetAllPqrsUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsAdminListBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PqrsAdapter { id ->
            findNavController().navigate(
                R.id.action_adminList_to_adminDetail,
                bundleOf("pqrsId" to id)
            )
        }

        b.rvAllPqrs.adapter = adapter

        lifecycleScope.launch {
            allUseCase().collect { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
