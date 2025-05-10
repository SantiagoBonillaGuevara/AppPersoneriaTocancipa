package com.personeriatocancipa.app.ui.admin.managementDates.editDate.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.databinding.FragmentCreateDateStep3Binding
import com.personeriatocancipa.app.domain.usecase.GetDatesByDayUseCase
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.EditDateActivity
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.EditDateViewModel
import com.personeriatocancipa.app.ui.user.createDate.CreateDateActivity
import com.personeriatocancipa.app.ui.user.createDate.CreateDateViewModel

class EditDateStep3Fragment: Fragment() {
    private val viewModel: EditDateViewModel by activityViewModels()
    private var _binding: FragmentCreateDateStep3Binding? = null
    private val binding get() = _binding!!
    private val getDatesByDayUseCase: GetDatesByDayUseCase = GetDatesByDayUseCase(FirebaseDateRepository())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i( "CreateDateStep3Fragment", "onViewCreated, estado actual de la cita: ${viewModel.date.value}")
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateDateStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI(){
        viewModel.hours.observe(viewLifecycleOwner) { horas ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, horas)
            binding.spinnerHora.setText("", false)
            binding.spinnerHora.setAdapter(adapter)
            viewModel.date.observe(viewLifecycleOwner){ date ->
                binding.spinnerHora.setText(date.hora, false)
                binding.txtDescripcion.setText(date.descripcion)
            }
        }
        binding.txtDescripcion.isEnabled = false
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }
        binding.btnAgendar.setOnClickListener {
            validate()
        }
        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun validate() {
        val hour = binding.spinnerHora.text.toString()
        if(hour.isEmpty()){
            Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.date.value?.hora = hour
        viewModel.date.value?.estado = "Pendiente"
        viewModel.isUserBooked()
        viewModel.userBooked.observe(viewLifecycleOwner, Observer { isBooked ->
            if (isBooked) Toast.makeText(requireContext(), "El usuario ya tiene una cita programada ese dia y hora", Toast.LENGTH_SHORT).show()
            else (activity as? EditDateActivity)?.saveDate()
        })
    }
}