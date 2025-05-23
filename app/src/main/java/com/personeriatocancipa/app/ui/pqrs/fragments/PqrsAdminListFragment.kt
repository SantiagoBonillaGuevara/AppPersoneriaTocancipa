// app/src/main/java/com/personeriatocancipa/app/ui/pqrs/fragments/PqrsAdminListFragment.kt
package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminListBinding
import com.personeriatocancipa.app.domain.usecase.GetAllPqrsUseCase
import com.personeriatocancipa.app.domain.usecase.GetPqrsWithUserUseCase
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.pqrs.adapter.PqrsAdminAdapter
import com.personeriatocancipa.app.ui.pqrs.viewmodel.PqrsAdminViewModel
import kotlinx.coroutines.flow.collectLatest

class PqrsAdminListFragment : Fragment(R.layout.fragment_pqrs_admin_list) {

    private var _b: FragmentPqrsAdminListBinding? = null
    private val b get() = _b!!

    private val viewModel: PqrsAdminViewModel by viewModels {
        // inyecta repositorios / usecases
        val pqrsRepo = FirebasePqrsRepository()
        val userRepo = FirebaseUserRepository()
        PqrsAdminViewModel.Factory(
            GetPqrsWithUserUseCase(
                GetAllPqrsUseCase(pqrsRepo),
                GetUserUseCase(userRepo)
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentPqrsAdminListBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        // Flecha volver
        b.toolbarAdminList.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = PqrsAdminAdapter { item ->
            val action = PqrsAdminListFragmentDirections
                .actionPqrsAdminListFragmentToPqrsAdminDetailFragment(item.id)
            findNavController().navigate(action)
        }
        b.rvPqrsAdmin.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        b.rvPqrsAdmin.adapter = adapter

        // Observa y actualiza lista
        lifecycleScope.launchWhenStarted {
            viewModel.pqrsUiList.collectLatest { list ->
                adapter.submitList(list)
                b.progressBarAdmin.visibility = View.GONE
            }
        }

        // Pull-to-refresh
        b.swipeRefreshAdmin.setOnRefreshListener {
            viewModel.loadPqrs()
            b.swipeRefreshAdmin.isRefreshing = false
        }

        // Carga inicial
        viewModel.loadPqrs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
