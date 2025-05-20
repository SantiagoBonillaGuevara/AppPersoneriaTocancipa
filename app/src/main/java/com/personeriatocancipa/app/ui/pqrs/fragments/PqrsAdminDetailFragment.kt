package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminDetailBinding
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.domain.usecase.GetPqrsByIdUseCase
import com.personeriatocancipa.app.domain.usecase.RespondPqrsUseCase
import kotlinx.coroutines.launch

class PqrsAdminDetailFragment : Fragment() {
    private var _b: FragmentPqrsAdminDetailBinding? = null
    private val b get() = _b!!
    private val args: PqrsAdminDetailFragmentArgs by navArgs()
    private val detailUseCase = GetPqrsByIdUseCase(FirebasePqrsRepository())
    private val respondUseCase = RespondPqrsUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsAdminDetailBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            detailUseCase.execute(args.pqrsId).onSuccess { p ->
                b.tvType.text = p.type
                b.tvDescription.text = p.description
                b.tvUserId.text = p.userId
            }
        }

        b.btnSendResponse.setOnClickListener {
            val resp = b.etResponse.text.toString().trim()
            if (resp.isNotEmpty()) {
                lifecycleScope.launch {
                    respondUseCase.execute(args.pqrsId, resp)
                        .onSuccess { findNavController().navigateUp() }
                        .onFailure {
                            Snackbar.make(b.root, "Error al enviar respuesta", Snackbar.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
