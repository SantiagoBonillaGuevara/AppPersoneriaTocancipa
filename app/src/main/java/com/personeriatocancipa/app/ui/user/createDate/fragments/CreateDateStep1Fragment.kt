package com.personeriatocancipa.app.ui.user.createDate.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentCreateDateStep1Binding
import com.personeriatocancipa.app.databinding.FragmentRegisterStep1Binding
import com.personeriatocancipa.app.domain.validators.DateValidator
import com.personeriatocancipa.app.ui.user.createDate.CreateDateActivity
import com.personeriatocancipa.app.ui.user.createDate.CreateDateViewModel
import java.util.Calendar

class CreateDateStep1Fragment : Fragment() {

    private val viewModel: CreateDateViewModel by activityViewModels()
    private var _binding: FragmentCreateDateStep1Binding? = null
    private val binding get() = _binding!!
    private lateinit var fecha: String
    private lateinit var tema: String
    private val validadorFecha = DateValidator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateDateStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.opcionesTema, android.R.layout.simple_dropdown_item_1line)
        viewModel.date.observe(viewLifecycleOwner) { date ->
            binding.spinnerCargo.setText("", false)
            binding.spinnerCargo.setAdapter(adapter)
            binding.spinnerCargo.setText(date.tema, false)
        }
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }
        binding.btnSiguiente.setOnClickListener {
            validate()
        }
        binding.txtFecha.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val fechaFormateada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                binding.txtFecha.setText(fechaFormateada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun validate() {
        tema = binding.spinnerCargo.text.toString()
        fecha = binding.txtFecha.text.toString()
        if (tema.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        (activity as? CreateDateActivity)?.getLawyers(tema, fecha)
        if (!validadorFecha.esFechaAnticipada(fecha)) {
            Toast.makeText(requireContext(), "La fecha debe ser agendada minimo una semana antes", Toast.LENGTH_SHORT).show()
            return
        }
        if (!validadorFecha.esFestivo(fecha)) {
            Toast.makeText(requireContext(), "No puedes agendar una cita en dias festivos", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.lawyerList.observe(viewLifecycleOwner, Observer { lawyerList ->
            Log.d("CreateDateStep1Fragment", "Abogados disponibles para el tema '$tema': ${lawyerList.size}")
            if (lawyerList.isNotEmpty()) {
                viewModel.date.value = viewModel.date.value?.copy(
                    tema = tema,
                    fecha = fecha,
                    correoCliente = viewModel.user.value?.correo,
                )
                (activity as? CreateDateActivity)?.navigateToNextStep(CreateDateStep2Fragment())
            } else Toast.makeText(requireContext(), "No hay abogados disponibles para el tema seleccionado", Toast.LENGTH_SHORT).show()
        })
    }

}