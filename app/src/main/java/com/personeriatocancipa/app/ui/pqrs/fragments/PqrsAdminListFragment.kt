package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminListBinding
import com.personeriatocancipa.app.domain.usecase.GetAllPqrsUseCase
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.ui.pqrs.adapter.PqrsAdminAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PqrsAdminListFragment : Fragment() {

    private var _b: FragmentPqrsAdminListBinding? = null
    private val b get() = _b!!
    private val adminUseCase = GetAllPqrsUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsAdminListBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.toolbarAdminList.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = PqrsAdminAdapter { id ->
            // <-- Aquí usamos el ID correcto de la acción:
            findNavController().navigate(
                R.id.action_pqrsAdminListFragment_to_pqrsAdminDetailFragment,
                bundleOf("pqrsId" to id)
            )
        }
        // Asigna la función al botón de navegación
        b.toolbarAdminList.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Muy importante: asignar el LayoutManagerz
        b.rvPqrsAdmin.layoutManager = LinearLayoutManager(requireContext())
        b.rvPqrsAdmin.adapter = adapter

        b.progressBarAdmin.visibility = View.VISIBLE

        // Pull-to-refresh manual
        b.swipeRefreshAdmin.setOnRefreshListener {
            lifecycleScope.launch {
                adminUseCase().collect { list ->
                    adapter.submitList(list)
                }
                b.swipeRefreshAdmin.isRefreshing = false
            }
        }

        // Carga inicial de datos
        lifecycleScope.launch {
            adminUseCase().collect { list ->
                adapter.submitList(list)
                b.progressBarAdmin.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
