package com.personeriatocancipa.app.ui.user.modify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterStep3Binding
import com.personeriatocancipa.app.ui.user.modify.ModifyUserActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyViewModel

class ModifyStep3Fragment: Fragment() {
    private val viewModel: ModifyViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep3Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCurrentUser()
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI() {
        val adapterSexo = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesSexo, android.R.layout.simple_dropdown_item_1line)
        val adapterIdentidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesIdentidad, android.R.layout.simple_dropdown_item_1line)
        val adapterOrientacion = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesOrientacion, android.R.layout.simple_dropdown_item_1line)
        val adapterNacionalidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesNacionalidad, android.R.layout.simple_dropdown_item_1line)
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.spinnerSexo.setText("", false)
            binding.spinnerIdentidad.setText("", false)
            binding.spinnerOrientacion.setText("", false)
            binding.spinnerNacionalidad.setText("", false)
            binding.spinnerSexo.setAdapter(adapterSexo)
            binding.spinnerIdentidad.setAdapter(adapterIdentidad)
            binding.spinnerOrientacion.setAdapter(adapterOrientacion)
            binding.spinnerNacionalidad.setAdapter(adapterNacionalidad)
            binding.spinnerSexo.setText(user.sexo, false)
            binding.spinnerIdentidad.setText(user.identidad, false)
            binding.spinnerOrientacion.setText(user.orientacion, false)
            binding.spinnerNacionalidad.setText(user.nacionalidad, false)
        }
        binding.step5.isVisible=false
    }

    private fun initComponents() {
        binding.spinnerSexo.setOnClickListener{
            binding.spinnerSexo.showDropDown()
        }

        binding.spinnerIdentidad.setOnClickListener{
            binding.spinnerIdentidad.showDropDown()
        }

        binding.spinnerOrientacion.setOnClickListener{
            binding.spinnerOrientacion.showDropDown()
        }

        binding.spinnerNacionalidad.setOnClickListener{
            binding.spinnerNacionalidad.showDropDown()
        }

        binding.btnSiguiente.setOnClickListener {
            navigateToNextStep()
        }

        binding.ivClose.setOnClickListener{
            activity?.finish()
        }

        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            sexo = binding.spinnerSexo.text.toString(),
            identidad = binding.spinnerIdentidad.text.toString(),
            orientacion = binding.spinnerOrientacion.text.toString(),
            nacionalidad = binding.spinnerNacionalidad.text.toString()
        )
        (activity as? ModifyUserActivity)?.navigateToNextStep(ModifyStep4Fragment())
    }
}