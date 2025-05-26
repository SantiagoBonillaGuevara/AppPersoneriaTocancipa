package com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.databinding.FragmentRegisterAdminPqrsStep2Binding
import com.personeriatocancipa.app.domain.usecase.RegisterUseCase
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.RegisterAdminPqrsActivity
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.RegisterAdminPqrsViewModel
import kotlinx.coroutines.launch

class RegisterAdminPqrsStep2Fragment : Fragment() {

    private var _b: FragmentRegisterAdminPqrsStep2Binding? = null
    private val b get() = _b!!
    private val vm: RegisterAdminPqrsViewModel by activityViewModels()

    // ← aquí, sólo pasamos el UserRepository
    private val registerUseCase = RegisterUseCase(
        FirebaseUserRepository()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRegisterAdminPqrsStep2Binding
        .inflate(inflater, container, false)
        .also { _b = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.txtContrasena.doOnTextChanged  { _,_,_,_ -> checkPasswords() }
        b.txtConfirmarContrasena.doOnTextChanged { _,_,_,_ -> checkPasswords() }

        b.ivClose   .setOnClickListener { requireActivity().finish() }
        b.btnVolver .setOnClickListener { requireActivity().onBackPressed() }
        b.btnSignup .setOnClickListener { signup() }
    }

    private fun checkPasswords(): Boolean {
        val p = b.txtContrasena   .text.toString()
        val c = b.txtConfirmarContrasena.text.toString()
        return when {
            p.isEmpty() || c.isEmpty() -> {
                b.txtIgualdad.text = ""
                false
            }
            p == c -> {
                b.txtIgualdad.text = "✔ Contraseñas coinciden"
                true
            }
            else -> {
                b.txtIgualdad.text = "✘ No coinciden"
                false
            }
        }
    }

    private fun signup() {
        if (!checkPasswords()) {
            Toast.makeText(requireContext(), "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }
        val pwd   = b.txtContrasena.text.toString()
        val admin = vm.admin.value ?: return

        lifecycleScope.launch {
            // aquí usamos exactamente la misma firma que tu RegisterUseCase
            registerUseCase
                .execute(pwd, admin, "AdminPqrsData")
                .onSuccess {
                    Toast.makeText(requireContext(), "¡Registrado!", Toast.LENGTH_SHORT).show()
                    (activity as? RegisterAdminPqrsActivity)?.finishOk()
                }
                .onFailure { e ->
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
