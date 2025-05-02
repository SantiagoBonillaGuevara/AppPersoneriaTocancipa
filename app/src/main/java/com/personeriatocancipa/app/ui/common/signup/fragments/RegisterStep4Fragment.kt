package com.personeriatocancipa.app.ui.common.signup.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterStep4Binding
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterViewModel

class RegisterStep4Fragment : Fragment() {
    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep4Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    private fun initUI() {
        val adapterEscolaridad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesEscolaridad, android.R.layout.simple_dropdown_item_1line)
        val adapterEtnico = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesGrupo, android.R.layout.simple_dropdown_item_1line)
        val adapterDiscapacidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesDiscapacidad, android.R.layout.simple_dropdown_item_1line)
        val adapterEstrato = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesEstrato, android.R.layout.simple_dropdown_item_1line)
        val adapterComunidad = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesComunidad, android.R.layout.simple_dropdown_item_1line)
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.spinnerEscolaridad.setText("", false)
            binding.spinnerEtnico.setText("", false)
            binding.spinnerDiscapacidad.setText("", false)
            binding.spinnerEstrato.setText("", false)
            binding.spinnerComunidad.setText("", false)

            binding.spinnerEscolaridad.setAdapter(adapterEscolaridad)
            binding.spinnerEtnico.setAdapter(adapterEtnico)
            binding.spinnerDiscapacidad.setAdapter(adapterDiscapacidad)
            binding.spinnerEstrato.setAdapter(adapterEstrato)
            binding.spinnerComunidad.setAdapter(adapterComunidad)

            binding.spinnerEscolaridad.setText(user.escolaridad, false)
            binding.spinnerEtnico.setText(user.grupoEtnico, false)
            binding.spinnerDiscapacidad.setText(user.discapacidad, false)
            binding.spinnerEstrato.setText(user.estrato, false)
            binding.spinnerComunidad.setText(user.comunidad, false)
        }
    }

    private fun initComponents() {
        binding.spinnerEscolaridad.setOnClickListener{
            binding.spinnerEscolaridad.showDropDown()
        }

        binding.spinnerEtnico.setOnClickListener{
            binding.spinnerEtnico.showDropDown()
        }

            binding.spinnerDiscapacidad.setOnClickListener{
            binding.spinnerDiscapacidad.showDropDown()
        }

        binding.spinnerEstrato.setOnClickListener{
            binding.spinnerEstrato.showDropDown()
        }

        binding.spinnerComunidad.setOnClickListener{
            binding.spinnerComunidad.showDropDown()
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
        val escolaridad = binding.spinnerEscolaridad.text.toString()
        val grupoEtnico = binding.spinnerEtnico.text.toString()
        val discapacidad = binding.spinnerDiscapacidad.text.toString()
        val estrato = binding.spinnerEstrato.text.toString()
        val comunidad = binding.spinnerComunidad.text.toString()
        if (escolaridad.isEmpty() || grupoEtnico.isEmpty() || discapacidad.isEmpty() || estrato.isEmpty() || comunidad.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        navigateToNextStep()
    }
    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            escolaridad = binding.spinnerEscolaridad.text.toString(),
            grupoEtnico = binding.spinnerEtnico.text.toString(),
            discapacidad = binding.spinnerDiscapacidad.text.toString(),
            estrato = binding.spinnerEstrato.text.toString(),
            comunidad = binding.spinnerComunidad.text.toString()
        )
        (activity as? RegisterActivity)?.navigateToNextStep(RegisterStep5Fragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStep4Binding.inflate(inflater, container, false)
        return binding.root
    }
}