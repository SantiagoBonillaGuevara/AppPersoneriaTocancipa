package com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep2Binding
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerViewModel

class RegisterLawyerStep2Fragment : Fragment() {
    private val viewModel: RegisterLawyerViewModel by activityViewModels()
    private var _binding: FragmentRegisterLawyerStep2Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterLawyerStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI(){
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesCargoAbogado, android.R.layout.simple_dropdown_item_1line)
        viewModel.lawyer.observe(viewLifecycleOwner) { lawyer ->
            binding.spinnerCargo.setText("", false)
            binding.spinnerCargo.setAdapter(adapter)
            binding.spinnerCargo.setText(lawyer.cargo, false)

            binding.spinnerCargo.setText(lawyer.cargo, false)

            // Limpiar el contenedor por si ya había checkboxes creados
            binding.checkboxContainer.removeAllViews()

            // Crear los checkboxes dinámicamente y marcar los que correspondan
            val temasSeleccionados = lawyer.temas?: emptyList() // <- lista de temas ya seleccionados

            for (tema in resources.getStringArray(R.array.opcionesTema)) {
                val checkBox = CheckBox(requireContext())
                checkBox.text = tema
                checkBox.isChecked = temasSeleccionados.contains(tema)
                binding.checkboxContainer.addView(checkBox)
            }
        }
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }
        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.btnSiguiente.setOnClickListener {
            validParams()
        }
    }

    private fun validParams(){
        val cargo = binding.spinnerCargo.text.toString()
        if(cargo.isEmpty() || getTemas().isEmpty()){
            Toast.makeText(requireContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        navigateToNextStep()
    }

    private fun getTemas(): List<String> {
        val temas = mutableListOf<String>()
        for (i in 0 until binding.checkboxContainer.childCount) {
            val checkBox = binding.checkboxContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                temas.add(checkBox.text.toString())
            }
        }
        return temas
    }

    private fun navigateToNextStep() {
        viewModel.lawyer.value = viewModel.lawyer.value?.copy(
            cargo = binding.spinnerCargo.text.toString(),
            temas = getTemas()
        )
        (activity as? RegisterLawyerActivity)?.navigateToNextStep(RegisterLawyerStep3Fragment())
    }
}