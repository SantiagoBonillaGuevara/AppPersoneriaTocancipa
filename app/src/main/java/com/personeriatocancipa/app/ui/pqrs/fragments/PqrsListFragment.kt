package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.google.firebase.auth.FirebaseAuth
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsListBinding
import com.personeriatocancipa.app.domain.usecase.GetMyPqrsUseCase
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.ui.pqrs.adapter.PqrsAdapter

class PqrsListFragment : Fragment() {

    private var _b: FragmentPqrsListBinding? = null
    private val b get() = _b!!
    private val listUseCase = GetMyPqrsUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsListBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val adapter = PqrsAdapter { id ->
            findNavController().navigate(
                R.id.action_list_to_detail,
                bundleOf("pqrsId" to id)
            )
        }

        b.rvPqrs.adapter = adapter

        b.swipeRefresh.setOnRefreshListener {
            b.swipeRefresh.isRefreshing = false // autoactualizado por Flow
        }

        lifecycleScope.launchWhenStarted {
            listUseCase(userId).collect { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
