package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsListBinding
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.domain.usecase.GetMyPqrsUseCase
import com.personeriatocancipa.app.ui.pqrs.adapter.PqrsAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        val adapter = PqrsAdapter { id ->
            findNavController().navigate(
                R.id.action_pqrsListFragment_to_pqrsDetailFragment,
                bundleOf("pqrsId" to id)
            )
        }

        b.rvPqrs.layoutManager = LinearLayoutManager(requireContext())
        b.rvPqrs.adapter       = adapter

    // 1) Obtén el userId
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Log.d("PqrsListFragment", "UID actual = $userId")

        // 3) Asigna LayoutManager y Adapter al RecyclerView
        b.rvPqrs.layoutManager = LinearLayoutManager(requireContext())
        b.rvPqrs.adapter = adapter

        // 4) Configura el SwipeRefresh como "loading"
        b.swipeRefresh.setOnRefreshListener {
            // simple redibujo: volver a lanzar la misma colección
            lifecycleScope.launch {
                listUseCase(userId).collect { list ->
                    adapter.submitList(list)
                    b.swipeRefresh.isRefreshing = false
                }
            }
        }

        // 5) Recoge el Flow por primera vez
        lifecycleScope.launch {
            listUseCase(userId).collect { list ->
                Log.d("PqrsListFragment", "Se encontraron ${list.size} PQRS")
                adapter.submitList(list)
                // si quieres quitar el spinner de pull-to-refresh:
                b.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
