package com.personeriatocancipa.app.ui.common.signup.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.databinding.FragmentRegisterStep5Binding
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterViewModel

class RegisterStep5Fragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep5Binding? = null
    private val binding get() = _binding!!

    private lateinit var password:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
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
        if(!binding.checkboxPolitica.isChecked){
            Toast.makeText(requireContext(), "Debes aceptar la política de tratamiento de datos personales", Toast.LENGTH_SHORT).show()
            return
        }
        registrar()
    }

    private fun registrar() {
        viewModel.user.value = viewModel.user.value?.copy(
            estado = "Activo"
        )
        (activity as? RegisterActivity)?.registerUser(password, viewModel.user.value!!) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }.onFailure {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterStep5Binding.inflate(inflater, container, false)
        return binding.root
    }
}