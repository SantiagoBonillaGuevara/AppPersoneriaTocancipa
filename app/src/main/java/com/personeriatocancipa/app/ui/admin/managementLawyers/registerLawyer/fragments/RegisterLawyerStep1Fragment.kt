package com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep1Binding
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerViewModel

class RegisterLawyerStep1Fragment : Fragment() {

    private val viewModel: RegisterLawyerViewModel by activityViewModels()
    private var _binding: FragmentRegisterLawyerStep1Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterLawyerStep1Binding.inflate(inflater, container, false)
        return binding.root
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
        val nombre = binding.txtNombre.text.toString() +" "+ binding.txtApellido.text.toString()
        val documento = binding.txtDocumento.text.toString()
        val correo = binding.txtCorreo.text.toString()

        if (nombre.isEmpty() || documento.isEmpty() || correo.isEmpty()) {
            Toast.makeText(requireContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        (activity as? RegisterLawyerActivity)?.validateParam("documento", documento) { existe ->
            if (existe) {
                Toast.makeText(requireContext(), "El documento ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                validateEmail(correo)
            }
        }
    }

    private fun validateEmail(email: String){
        (activity as? RegisterLawyerActivity)?.validateParam("correo", email) { existe ->
            if (existe) {
                Toast.makeText(requireContext(), "El correo ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                navigateToNextStep()
            }
        }
    }
    private fun navigateToNextStep() {
        viewModel.lawyer.value = viewModel.lawyer.value?.copy(
            nombreCompleto = binding.txtNombre.text.toString() + " " + binding.txtApellido.text.toString(),
            documento = binding.txtDocumento.text.toString(),
            correo = binding.txtCorreo.text.toString()
        )
        (activity as? RegisterLawyerActivity)?.navigateToNextStep(RegisterLawyerStep2Fragment())
    }
}