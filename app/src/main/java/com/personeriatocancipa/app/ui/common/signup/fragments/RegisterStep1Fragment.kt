package com.personeriatocancipa.app.ui.common.signup.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentRegisterStep1Binding
import com.personeriatocancipa.app.ui.common.signup.RegisterActivity
import com.personeriatocancipa.app.ui.common.signup.RegisterViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar

class RegisterStep1Fragment : Fragment() {
    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep1Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    private fun initUI() {
        val adapterTipoDoc = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesTipoDocumento, android.R.layout.simple_dropdown_item_1line)
        binding.spinnerTipoDocumento.setAdapter(adapterTipoDoc)
    }

    private fun initComponents(){
        binding.spinnerTipoDocumento.setOnClickListener {
            binding.spinnerTipoDocumento.showDropDown()
        }

        binding.btnSiguiente.setOnClickListener{
            validParams()
        }

        binding.btnCancelar.setOnClickListener{
            activity?.finish()
        }

        binding.txtFechaNacimiento.setOnClickListener{
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val fechaFormateada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                binding.txtFechaNacimiento.setText(fechaFormateada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun validParams() {
        val tipoDocumento = binding.spinnerTipoDocumento.text.toString()
        val documento = binding.txtNumeroDocumento.text.toString()
        val fechaNacimiento = binding.txtFechaNacimiento.text.toString()
        val nombre = binding.txtNombre.text.toString()
        val apellido = binding.txtApellido.text.toString()
        if (tipoDocumento.isEmpty() || documento.isEmpty() || fechaNacimiento.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        navigateToNextStep()
    }

    private fun navigateToNextStep() {
        viewModel.user.value = viewModel.user.value?.copy(
            tipoDocumento = binding.spinnerTipoDocumento.text.toString(),
            documento = binding.txtNumeroDocumento.text.toString(),
            fechaNacimiento = binding.txtFechaNacimiento.text.toString(),
            nombreCompleto = binding.txtNombre.text.toString()+" "+binding.txtApellido.text.toString(),
            edad = calcularEdad(binding.txtFechaNacimiento.text.toString()),
            grupoEtario = calcularGrupoEtario(calcularEdad(binding.txtFechaNacimiento.text.toString()))
        )
        (activity as? RegisterActivity)?.navigateToNextStep(RegisterStep2Fragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    fun calcularEdad(fechaNacimiento: String): Int {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fecha = LocalDate.parse(fechaNacimiento, formato)
        return Period.between(fecha, LocalDate.now()).years
    }

    fun calcularGrupoEtario(edad: Int): String = when {
        edad < 6 -> "Primera infancia"
        edad in 6..11 -> "Infancia"
        edad in 12..17 -> "Adolescencia"
        edad in 18..26 -> "Juventud"
        edad in 27..59 -> "Adultez"
        else -> "Persona mayor"
    }
}