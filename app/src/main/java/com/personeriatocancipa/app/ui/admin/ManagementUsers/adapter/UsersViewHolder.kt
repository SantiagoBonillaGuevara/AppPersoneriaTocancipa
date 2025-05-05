package com.personeriatocancipa.app.ui.admin.ManagementUsers.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemUserBinding
import com.personeriatocancipa.app.domain.model.User

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemUserBinding.bind(view)
    @SuppressLint("SetTextI18n")
    fun bind(user: User, onUserChanged: (User, Boolean) -> Unit, onEditClicked: (User) -> Unit) {
        binding.tvName.text = user.nombreCompleto
        binding.tvEmail.text = user.correo
        binding.tvTelefono.text = user.telefono
        binding.tvTipoDocumento.text = user.tipoDocumento+" - "
        binding.tvDocumento.text = user.documento
        binding.tvPais.text = user.nacionalidad
        binding.tvSexo.text = user.sexo
        binding.tvAge.text = user.edad.toString()
        binding.tvEtario.text = user.grupoEtario
        binding.tvIdentidad.text = user.identidad
        binding.tvOrientacion.text = user.orientacion
        binding.tvSector.text = user.sector
        binding.tvComunidad.text = user.comunidad
        binding.tvEstrato.text = user.estrato
        binding.tvDiscapacidad.text = user.discapacidad
        binding.tvEscolaridad.text = user.escolaridad
        binding.tvGrupoEtnico.text = user.grupoEtnico
        binding.tvDireccion.text = user.direccion

        binding.switchUser.setOnCheckedChangeListener(null)
        binding.switchUser.isChecked = user.estado == "Activo"

        binding.switchUser.setOnCheckedChangeListener { _, isChecked ->
            onUserChanged(user, isChecked)
        }

        binding.ivEdit.setOnClickListener {
            onEditClicked(user)
        }
    }

}