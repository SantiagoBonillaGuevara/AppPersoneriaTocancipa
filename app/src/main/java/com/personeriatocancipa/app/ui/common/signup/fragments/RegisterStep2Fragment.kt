package com.personeriatocancipa.app.ui.common.signup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterStep2Binding
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegisterStep2Fragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()

    private var _binding: FragmentRegisterStep2Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    private fun initUI() {
        val adapterSector = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesSector, android.R.layout.simple_dropdown_item_1line)
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.spinnerSector.setText("", false)
            binding.spinnerSector.setAdapter(adapterSector)
            binding.spinnerSector.setText(user.sector, false)
            binding.txtDireccion.setText(user.direccion)
            binding.txtCorreo.setText(user.correo)
            binding.txtTelefono.setText(user.telefono)
        }
    }

    private fun initComponents() {
        binding.spinnerSector.setOnClickListener{
            binding.spinnerSector.showDropDown()
        }

        binding.btnSiguiente.setOnClickListener {
            validParams()
        }

        binding.ivClose.setOnClickListener{
            activity?.finish()
        }

        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun validParams() {
        val sector = binding.spinnerSector.text.toString()
        val direccion = binding.txtDireccion.text.toString()
        val correo = binding.txtCorreo.text.toString()
        val telefono = binding.txtTelefono.text.toString()
        if (sector.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        (activity as? RegisterActivity)?.validateParam("correo", correo) { existe ->
            if (existe) {
                Toast.makeText(requireContext(), "El correo ya está registrado", Toast.LENGTH_SHORT).show()
            } else {
                navigateToNextStep()
            }
        }
    }

    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            sector = binding.spinnerSector.text.toString(),
            direccion = binding.txtDireccion.text.toString(),
            correo = binding.txtCorreo.text.toString(),
            telefono = binding.txtTelefono.text.toString(),
        )
        (activity as? RegisterActivity)?.navigateToNextStep(RegisterStep3Fragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }
}