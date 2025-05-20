package com.personeriatocancipa.app.ui.pqrs.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsCreateBinding
import com.personeriatocancipa.app.domain.model.Pqrs
import com.personeriatocancipa.app.domain.usecase.CreatePqrsUseCase
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import kotlinx.coroutines.launch
import java.util.UUID

class PqrsCreateFragment : Fragment() {

    private var _binding: FragmentPqrsCreateBinding? = null
    private val binding get() = _binding!!

    // Inicializa tu UseCase correctamente
    private val createPqrsUseCase by lazy {
        CreatePqrsUseCase(FirebasePqrsRepository())
    }

    private lateinit var pickFileLauncher: ActivityResultLauncher<String>
    private var attachedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.tvAttached.text = it.lastPathSegment
                attachedFileUri = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPqrsCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spinner de tipos
        val types = listOf("Petición", "Queja", "Reclamo", "Sugerencia")
        binding.spinnerType.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)

        // Adjuntar archivo (placeholder)
        binding.btnAttach.setOnClickListener {
            Toast.makeText(requireContext(), "Funcionalidad de adjuntar pendiente", Toast.LENGTH_SHORT).show()
        }

        binding.btnAttach.setOnClickListener {
            pickFileLauncher.launch("*/*")
        }

        // Enviar PQRS
        binding.btnSend.setOnClickListener {
            val type = binding.spinnerType.selectedItem.toString()
            val title = binding.etTitle.text.toString().trim()
            val desc  = binding.etDescription.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                ?: return@setOnClickListener Toast
                    .makeText(requireContext(),"Usuario no autenticado",Toast.LENGTH_SHORT)
                    .show()

            val pqrs = Pqrs(
                id = UUID.randomUUID().toString(),
                userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                type = type,
                title = title,
                description = desc,
                attachment = attachedFileUri?.toString() ?: "",
                date = System.currentTimeMillis()
            )


            // Llamada al UseCase
            lifecycleScope.launch {
                createPqrsUseCase.execute(pqrs)
                    .onSuccess {
                        Toast.makeText(requireContext(), "PQRS enviada correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.pqrsListFragment)
                    }
                    .onFailure {
                        Snackbar.make(binding.root, "Error al enviar PQRS", Snackbar.LENGTH_LONG).show()
                    }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
