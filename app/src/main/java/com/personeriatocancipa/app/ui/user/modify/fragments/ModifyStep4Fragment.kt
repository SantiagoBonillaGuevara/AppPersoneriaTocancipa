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
import com.personeriatocancipa.app.databinding.FragmentRegisterStep4Binding
import com.personeriatocancipa.app.ui.user.modify.ModifyUserActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyViewModel

class ModifyStep4Fragment: Fragment() {
    private val viewModel: ModifyViewModel by activityViewModels()
    private var _binding: FragmentRegisterStep4Binding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCurrentUser()
        initUI()
        initComponents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterStep4Binding.inflate(inflater, container, false)
        return binding.root
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
        binding.btnSiguiente.text = "Actualizar datos"
        binding.step5.isVisible=false
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
            modify()
        }

        binding.ivClose.setOnClickListener{
            activity?.finish()
        }

        binding.btnVolver.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun modify() {
        viewModel.user.value = viewModel.user.value?.copy(
            escolaridad = binding.spinnerEscolaridad.text.toString(),
            grupoEtnico = binding.spinnerEtnico.text.toString(),
            discapacidad = binding.spinnerDiscapacidad.text.toString(),
            estrato = binding.spinnerEstrato.text.toString(),
            comunidad = binding.spinnerComunidad.text.toString(),
        )
        (activity as? ModifyUserActivity)?.modifyUser(viewModel.user.value!!) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Datos actualizados con éxito", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }.onFailure {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}