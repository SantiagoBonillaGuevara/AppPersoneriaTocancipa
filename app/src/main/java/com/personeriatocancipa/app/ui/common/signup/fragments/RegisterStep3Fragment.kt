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
import com.personeriatocancipa.app.databinding.FragmentRegisterStep3Binding
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterViewModel

class RegisterStep3Fragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()

    private var _binding: FragmentRegisterStep3Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    private fun initUI() {
        val adapterSexo = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesSexo, android.R.layout.simple_dropdown_item_1line)
        val adapterIdentidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesIdentidad, android.R.layout.simple_dropdown_item_1line)
        val adapterOrientacion = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesOrientacion, android.R.layout.simple_dropdown_item_1line)
        val adapterNacionalidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesNacionalidad, android.R.layout.simple_dropdown_item_1line)
        binding.spinnerSexo.setAdapter(adapterSexo)
        binding.spinnerIdentidad.setAdapter(adapterIdentidad)
        binding.spinnerOrientacion.setAdapter(adapterOrientacion)
        binding.spinnerNacionalidad.setAdapter(adapterNacionalidad)
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
            validParams()
        }

        binding.btnCancelar.setOnClickListener{
            activity?.finish()
        }

        binding.ivBack.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun validParams() {
        val sexo = binding.spinnerSexo.text.toString()
        val identidad = binding.spinnerIdentidad.text.toString()
        val orientacion = binding.spinnerOrientacion.text.toString()
        val nacionalidad = binding.spinnerNacionalidad.text.toString()
        if (sexo.isEmpty() || identidad.isEmpty() || orientacion.isEmpty() || nacionalidad.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        navigateToNextStep()
    }
    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            sexo = binding.spinnerSexo.text.toString(),
            identidad = binding.spinnerIdentidad.text.toString(),
            orientacion = binding.spinnerOrientacion.text.toString(),
            nacionalidad = binding.spinnerNacionalidad.text.toString()
        )
        (activity as? RegisterActivity)?.navigateToNextStep(RegisterStep4Fragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterStep3Binding.inflate(inflater, container, false)
        return binding.root
    }
}