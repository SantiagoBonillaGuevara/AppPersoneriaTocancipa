package com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep1Binding
import com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer.ModifyLawyerActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.modifyLawyer.ModifyLawyerViewModel

class ModifyLawyerStep1Fragment:Fragment() {

    private val viewModel: ModifyLawyerViewModel by activityViewModels()
    private var _binding: FragmentRegisterLawyerStep1Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usuario = arguments?.getString("lawyer")
        viewModel.loadCurrentUser(usuario)
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterLawyerStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI() {
        viewModel.lawyer.observe(viewLifecycleOwner) { lawyer ->
            val partes = lawyer.nombreCompleto?.split(" ") ?: listOf()
            val nombre = partes.getOrNull(0) ?: ""
            val apellido = partes.getOrNull(1) ?: ""
            binding.txtNombre.setText(nombre)
            binding.txtApellido.setText(apellido)
            binding.txtDocumento.setText(lawyer.documento)
            binding.txtCorreo.setText(lawyer.correo)
        }
        binding.txtCorreo.isEnabled=false
        binding.step4.isVisible=false
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }

        binding.btnSiguiente.setOnClickListener {
            validParams()
        }
    }

    private fun validParams() {
        val nombre = binding.txtNombre.text.toString()
        val documento = binding.txtDocumento.text.toString()
        val apellido = binding.txtApellido.text.toString()

        if (nombre.isEmpty() || documento.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(requireContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if(viewModel.lawyer.value!!.documento.toString() == documento) {
            navigateToNextStep()
            return
        }
        (activity as? ModifyLawyerActivity)?.validateParam("documento", documento) { existe ->
            if (existe) {
                Toast.makeText(requireContext(), "El documento ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                navigateToNextStep()
            }
        }
    }

    private fun navigateToNextStep() {
        viewModel.lawyer.value = viewModel.lawyer.value?.copy(
            nombreCompleto = binding.txtNombre.text.toString() + " " + binding.txtApellido.text.toString(),
            documento = binding.txtDocumento.text.toString()
        )
        (activity as? ModifyLawyerActivity)?.navigateToNextStep(ModifyLawyerStep2Fragment())
    }
}