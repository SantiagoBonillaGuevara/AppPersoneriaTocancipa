// app/src/main/java/com/personeriatocancipa/app/ui/pqrs/fragments/PqrsAdminDetailFragment.kt
package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.FragmentPqrsAdminDetailBinding
import com.personeriatocancipa.app.domain.usecase.GetPqrsByIdUseCase
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.domain.usecase.RespondPqrsUseCase
import com.personeriatocancipa.app.ui.pqrs.viewmodel.PqrsAdminDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PqrsAdminDetailFragment : Fragment(R.layout.fragment_pqrs_admin_detail) {

    private var _binding: FragmentPqrsAdminDetailBinding? = null
    private val b get() = _binding!!

    // 1) Recibimos el argumento con el ID de la PQRS
    private val args: PqrsAdminDetailFragmentArgs by navArgs()

    // 2) Creamos el ViewModel inyectando los use-cases necesarios
    private val viewModel: PqrsAdminDetailViewModel by viewModels {
        // repositorios
        val pqrsRepo  = FirebasePqrsRepository()
        val userRepo  = FirebaseUserRepository()
        PqrsAdminDetailViewModel.Factory(
            GetPqrsByIdUseCase(pqrsRepo),
            GetUserUseCase(userRepo),
            RespondPqrsUseCase(pqrsRepo)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPqrsAdminDetailBinding.bind(view)

        // 3) Flecha de "volver"
        b.toolbarAdminDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // 4) Cargar datos de la PQRS + nombre de usuario
        viewModel.loadPqrsById(args.pqrsId)
        viewModel.pqrsDetail.observe(viewLifecycleOwner) { detail ->
            b.tvDetailUserName.text = detail.userName
            b.tvDetailType.text     = detail.type
            b.tvDetailDate.text     = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            ).format(Date(detail.date))
            b.tvDetailTitle.text       = detail.title
            b.tvDetailDescription.text = detail.description
        }

        // 5) Enviar la respuesta y volver al listado
        b.btnSendResponse.setOnClickListener {
            val respuesta = b.etResponse.text.toString().trim()
            if (respuesta.isNotEmpty()) {
                viewModel.sendResponse(args.pqrsId, respuesta)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
