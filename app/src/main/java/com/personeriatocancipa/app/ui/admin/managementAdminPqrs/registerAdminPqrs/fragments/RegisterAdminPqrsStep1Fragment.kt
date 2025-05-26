package com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep1Binding
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.RegisterAdminPqrsActivity
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.RegisterAdminPqrsViewModel

class RegisterAdminPqrsStep1Fragment : Fragment() {
    private var _b: FragmentRegisterLawyerStep1Binding? = null
    private val b get() = _b!!
    private val vm: RegisterAdminPqrsViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) =
        FragmentRegisterLawyerStep1Binding.inflate(i,c,false).also{_b=it}.root

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v,s)
        b.ivClose.setOnClickListener { requireActivity().finish() }
        b.btnSiguiente.setOnClickListener { next() }
    }

    private fun next() {
        val nom = b.txtNombre.text.toString().trim()
        val ape = b.txtApellido.text.toString().trim()
        val doc = b.txtDocumento.text.toString().trim()
        val mail= b.txtCorreo.text.toString().trim()
        if(nom.isEmpty()||ape.isEmpty()||doc.isEmpty()||mail.isEmpty()){
            Toast.makeText(requireContext(),"Llene todos los campos",Toast.LENGTH_SHORT).show()
            return
        }
        vm.admin.value = vm.admin.value?.copy(
            nombreCompleto = "$nom $ape",
            documento      = doc,
            correo         = mail,
            estado         = "Activo"
        )
        (activity as? RegisterAdminPqrsActivity)
            ?.navigateNext(RegisterAdminPqrsStep2Fragment())
    }
}
