package com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep3Binding
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep4Binding
import com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer.RegisterLawyerActivity
import com.personeriatocancipa.app.ui.admin.ManagementLawyers.registerLawyer.RegisterLawyerViewModel
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity

class RegisterLawyerStep4Fragment : Fragment() {

    private val viewModel: RegisterLawyerViewModel by activityViewModels()
    private var _binding: FragmentRegisterLawyerStep4Binding? = null
    private val binding get() = _binding!!
    private lateinit var password:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterLawyerStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initComponents(){
        binding.txtContrasena.doOnTextChanged { _, _, _, _  -> verificarCoincidencia() }
        binding.txtConfirmarContrasena.doOnTextChanged { _, _, _, _  -> verificarCoincidencia() }

        binding.btnSignup.setOnClickListener {
            validarRegistro()
        }
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }
        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun verificarCoincidencia():Boolean{
        password = binding.txtContrasena.text.toString()
        val confirm = binding.txtConfirmarContrasena.text.toString()
        when {
            password.isEmpty() || confirm.isEmpty() -> {
                binding.txtIgualdad.text = ""
                return false
            }
            password == confirm -> {
                binding.txtIgualdad.text = "✔ Contraseñas coinciden"
                binding.txtIgualdad.setTextColor(Color.parseColor("#2E7D32")) // verde
                return true
            }
            else -> {
                binding.txtIgualdad.text = "✘ Las contraseñas no coinciden"
                binding.txtIgualdad.setTextColor(Color.parseColor("#C62828")) // rojo
                return false
            }
        }
    }

    private fun validarRegistro() {
        if(!verificarCoincidencia()){
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }
        registrar()
    }

    private fun registrar() {
        viewModel.lawyer.value = viewModel.lawyer.value?.copy(
            estado = "Activo"
        )
        (activity as? RegisterLawyerActivity)?.registerLawyer(password, viewModel.lawyer.value!!) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }.onFailure {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}