package com.personeriatocancipa.app.ui.user.modify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterStep2Binding
import com.personeriatocancipa.app.ui.user.modify.ModifyActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyViewModel

class ModifyStep2Fragment: Fragment() {
    private val viewModel: ModifyViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep2Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCurrentUser()
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI() {
        val adapterSector = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesSector, android.R.layout.simple_dropdown_item_1line)
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.txtDireccion.setText(user.direccion)
            binding.spinnerSector.setText("", false)
            binding.spinnerSector.setAdapter(adapterSector)
            binding.spinnerSector.setText(user.sector, false)
            binding.txtCorreo.setText(user.correo)
            binding.txtTelefono.setText(user.telefono)
        }
        binding.txtCorreo.isEnabled=false
        binding.step5.isVisible=false
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
        val direccion = binding.txtDireccion.text.toString()
        val telefono = binding.txtTelefono.text.toString()
        if (direccion.isEmpty() || telefono.isEmpty())Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        else navigateToNextStep()
    }

    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            sector = binding.spinnerSector.text.toString(),
            direccion = binding.txtDireccion.text.toString(),
            correo = binding.txtCorreo.text.toString(),
            telefono = binding.txtTelefono.text.toString(),
        )
        (activity as? ModifyActivity)?.navigateToNextStep(ModifyStep3Fragment())
    }
}