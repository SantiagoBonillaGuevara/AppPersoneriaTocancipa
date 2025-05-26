package com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep3Binding
import com.personeriatocancipa.app.domain.model.Horario
import com.personeriatocancipa.app.domain.model.HorarioDia
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerActivity
import com.personeriatocancipa.app.ui.admin.managementLawyers.registerLawyer.RegisterLawyerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterLawyerStep3Fragment : Fragment() {

    private val viewModel: RegisterLawyerViewModel by activityViewModels()
    private var _binding: FragmentRegisterLawyerStep3Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterLawyerStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initUI(){
        viewModel.lawyer.observe(viewLifecycleOwner) { lawyer ->
            binding.etInicioLunes.setText(lawyer.horario?.Lunes?.inicio?: "")
            binding.etFinLunes.setText(lawyer.horario?.Lunes?.fin?: "")
            binding.etInicioMartes.setText(lawyer.horario?.Martes?.inicio?: "")
            binding.etFinMartes.setText(lawyer.horario?.Martes?.fin?: "")
            binding.etInicioMiercoles.setText(lawyer.horario?.Miércoles?.inicio?: "")
            binding.etFinMiercoles.setText(lawyer.horario?.Miércoles?.fin?: "")
            binding.etInicioJueves.setText(lawyer.horario?.Jueves?.inicio?: "")
            binding.etFinJueves.setText(lawyer.horario?.Jueves?.fin?: "")
            binding.etInicioViernes.setText(lawyer.horario?.Viernes?.inicio?: "")
            binding.etFinViernes.setText(lawyer.horario?.Viernes?.fin?: "")
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
            if (validParams()) navigateToNextStep()
        }

        binding.etFinLunes.setOnClickListener { mostrarListaHoras(binding.etFinLunes) }
        binding.etInicioLunes.setOnClickListener { mostrarListaHoras(binding.etInicioLunes) }
        binding.etFinMartes.setOnClickListener { mostrarListaHoras(binding.etFinMartes) }
        binding.etInicioMartes.setOnClickListener { mostrarListaHoras(binding.etInicioMartes) }
        binding.etFinMiercoles.setOnClickListener { mostrarListaHoras(binding.etFinMiercoles) }
        binding.etInicioMiercoles.setOnClickListener { mostrarListaHoras(binding.etInicioMiercoles) }
        binding.etFinJueves.setOnClickListener { mostrarListaHoras(binding.etFinJueves) }
        binding.etInicioJueves.setOnClickListener { mostrarListaHoras(binding.etInicioJueves) }
        binding.etFinViernes.setOnClickListener { mostrarListaHoras(binding.etFinViernes) }
        binding.etInicioViernes.setOnClickListener { mostrarListaHoras(binding.etInicioViernes) }
    }

    private fun validParams(): Boolean {
        val dias = listOf(
            Pair(binding.etInicioLunes.text.toString(), binding.etFinLunes.text.toString()),
            Pair(binding.etInicioMartes.text.toString(), binding.etFinMartes.text.toString()),
            Pair(binding.etInicioMiercoles.text.toString(), binding.etFinMiercoles.text.toString()),
            Pair(binding.etInicioJueves.text.toString(), binding.etFinJueves.text.toString()),
            Pair(binding.etInicioViernes.text.toString(), binding.etFinViernes.text.toString())
        )
        var alMenosUnDiaValido = false
        for ((index, dia) in dias.withIndex()) {
            val (inicio, fin) = dia
            if (inicio.isNotEmpty() || fin.isNotEmpty()) {
                if (inicio.isEmpty() || fin.isEmpty()) {
                    Toast.makeText(requireContext(), "Completa tanto la hora de inicio como la de fin para el día ${nombreDia(index)}", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (!horaValida(inicio, fin)) {
                    Toast.makeText(requireContext(), "La hora de inicio debe ser menor que la de fin para el día ${nombreDia(index)}", Toast.LENGTH_SHORT).show()
                    return false
                }
                alMenosUnDiaValido = true
            }
        }
        if (!alMenosUnDiaValido) {
            Toast.makeText(requireContext(), "Debes diligenciar al menos un día con horario válido", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun horaValida(inicio: String, fin: String): Boolean {
        val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
        val horaInicio = formato.parse(inicio)
        val horaFin = formato.parse(fin)
        return horaInicio!!.before(horaFin)
    }

    fun mostrarListaHoras(editText: EditText) {
        val horas = (7..18).map { String.format("%02d:00", it) }.toMutableList()
        horas.add("Borrar hora") // opción adicional
        AlertDialog.Builder(editText.context)
            .setTitle("Selecciona una hora")
            .setItems(horas.toTypedArray()) { _, which ->
                if (horas[which] == "Borrar hora") {
                    editText.setText("")
                } else {
                    editText.setText(horas[which])
                }
            }
            .show()
    }

    private fun nombreDia(index: Int): String {
        return when (index) {
            0 -> "Lunes"
            1 -> "Martes"
            2 -> "Miércoles"
            3 -> "Jueves"
            4 -> "Viernes"
            else -> "Día desconocido"
        }
    }

    private fun navigateToNextStep() {
        viewModel.lawyer.value = viewModel.lawyer.value?.copy(
            horario = getHorario()
        )
        (activity as? RegisterLawyerActivity)?.navigateToNextStep(RegisterLawyerStep4Fragment())
    }

    private fun getHorario(): Horario {
        val horario = Horario(
            Lunes = HorarioDia(
                inicio = binding.etInicioLunes.text.toString(),
                fin = binding.etFinLunes.text.toString()
            ),
            Martes = HorarioDia(
                inicio = binding.etInicioMartes.text.toString(),
                fin = binding.etFinMartes.text.toString()
            ),
            Miércoles = HorarioDia(
                inicio = binding.etInicioMiercoles.text.toString(),
                fin = binding.etFinMiercoles.text.toString()
            ),
            Jueves = HorarioDia(
                inicio = binding.etInicioJueves.text.toString(),
                fin = binding.etFinJueves.text.toString()
            ),
            Viernes = HorarioDia(
                inicio = binding.etInicioViernes.text.toString(),
                fin = binding.etFinViernes.text.toString()
            )
        )
        return horario
    }
}