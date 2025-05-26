package com.personeriatocancipa.app.ui.admin.managementAdminPqrs

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.databinding.FragmentRegisterLawyerStep1Binding
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.domain.usecase.ModifyUseCase
import kotlinx.coroutines.launch

class ModifyAdminPqrsFragment : Fragment() {

    private var _b: FragmentRegisterLawyerStep1Binding? = null
    private val b get() = _b!!
    private var current = Admin(null,"","","","")

    private val getUser = GetUserUseCase   (FirebaseUserRepository())
    private val modify  = ModifyUseCase    (FirebaseUserRepository())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) =
        FragmentRegisterLawyerStep1Binding.inflate(i,c,false).also{_b=it}.root

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v,s)
        val uid = arguments?.getString("uid").orEmpty()
        if (uid.isNotBlank()) {
            b.txtCorreo.isEnabled = false
            lifecycleScope.launch {
                getUser.execute("AdminPqrsData",uid)
                    .onSuccess { usr->
                        if(usr is Admin) {
                            current = usr
                            val p = usr.nombreCompleto?.split(" ")?:listOf("","")
                            b.txtNombre   .setText(p.getOrNull(0))
                            b.txtApellido .setText(p.getOrNull(1))
                            b.txtDocumento.setText(usr.documento)
                            b.txtCorreo   .setText(usr.correo)
                        }
                    }
            }
        }
        b.ivClose     .setOnClickListener { requireActivity().finish() }
        b.btnSiguiente.setOnClickListener { save() }
    }

    private fun save() {
        val nom  = b.txtNombre.text.toString().trim()
        val ape  = b.txtApellido.text.toString().trim()
        val doc  = b.txtDocumento.text.toString().trim()
        val mail = b.txtCorreo.text.toString().trim()
        if(nom.isEmpty()||ape.isEmpty()||doc.isEmpty()||mail.isEmpty()) {
            Toast.makeText(requireContext(),"Llene todos los campos",Toast.LENGTH_SHORT).show()
            return
        }
        current = current.copy(
            nombreCompleto = "$nom $ape",
            documento      = doc,
            correo         = mail,
            estado         = current.estado?:"Activo"
        )
        lifecycleScope.launch {
            modify.execute(current,"AdminPqrsData")
                .onSuccess {
                    Toast.makeText(requireContext(),"¡Guardado!",Toast.LENGTH_SHORT).show()
                    (activity as? ModifyAdminPqrsActivity)?.finishOk()
                }
                .onFailure{e->
                    Toast.makeText(requireContext(),"Error: ${e.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }
}
