package com.personeriatocancipa.app.ui.admin.managementDates.editDate.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.personeriatocancipa.app.databinding.FragmentCreateDateStep2Binding
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.validators.LawyerAvailabilityValidator
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.EditDateActivity
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.EditDateViewModel
import com.personeriatocancipa.app.ui.user.createDate.adapter.LawyersAvailableAdapter

class EditDateStep2Fragment: Fragment() {

    private val viewModel: EditDateViewModel by activityViewModels()
    private var _binding: FragmentCreateDateStep2Binding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LawyersAvailableAdapter
    private val validadorHoras = LawyerAvailabilityValidator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateDateStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI(){
        viewModel.lawyerList.observe(viewLifecycleOwner) { lawyerList ->
            // Actualizamos el adapter solo cuando 'lawyerList' cambia
            adapter = LawyersAvailableAdapter(lawyerList) { lawyer ->
                viewModel.lawyer.value = lawyer
            }
            binding.rvLawyers.layoutManager = LinearLayoutManager(requireContext())
            binding.rvLawyers.adapter = adapter
        }
    }

    private fun initComponents() {
        binding.ivClose.setOnClickListener {
            activity?.finish()
        }
        binding.btnSiguiente.setOnClickListener {
            validate()
        }
        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun validate() {
        val selectedLawyer = viewModel.lawyer.value
        if (selectedLawyer?.correo.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Seleccione un abogado", Toast.LENGTH_SHORT).show()
            return
        }
        getHours(selectedLawyer!!)
    }

    private fun getHours(lawyer: Lawyer) {
        viewModel.getDatesByDay("correoAbogado", lawyer.correo!!)
        viewModel.dates.observe(viewLifecycleOwner, Observer { dates ->
            val hours = validadorHoras.getAvailableHoursForAppointment(lawyer, viewModel.date.value?.fecha!!, dates)
            if (hours.isNotEmpty()) {
                viewModel.hours.value = hours
                viewModel.lawyer.value = lawyer
                viewModel.date.value!!.correoAbogado = lawyer.correo
                navigateToNextStep()
            }
            else Toast.makeText(requireContext(), "No hay horas disponibles para la fecha seleccionada con ese abogado", Toast.LENGTH_SHORT).show()
        })
    }

    private fun navigateToNextStep() {
        (activity as EditDateActivity).navigateToNextStep(EditDateStep3Fragment())
    }
}