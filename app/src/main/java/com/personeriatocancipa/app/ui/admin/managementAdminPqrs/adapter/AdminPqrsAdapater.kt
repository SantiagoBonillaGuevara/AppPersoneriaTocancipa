package com.personeriatocancipa.app.ui.admin.managementAdminPqrs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemUserBinding
import com.personeriatocancipa.app.domain.model.Admin

class AdminPqrsAdapter(
    private var items: List<Admin>,
    private val onEdit : (Admin)->Unit,
    private val onState: (Admin,Boolean)->Unit
) : RecyclerView.Adapter<AdminPqrsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, pos: Int) =
        holder.bind(items[pos], onEdit, onState)

    fun updateList(newList: List<Admin>) {
        items = newList
        notifyDataSetChanged()
    }

    class VH(private val b: ItemUserBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(adm: Admin,
                 onEdit: (Admin)->Unit,
                 onState: (Admin,Boolean)->Unit
        ) {
            b.tvName      .text = adm.nombreCompleto
            b.tvEmail     .text = adm.correo
            b.tvDocumento .text = adm.documento
            b.switchUser.setOnCheckedChangeListener(null)
            b.switchUser.isChecked = adm.estado=="Activo"
            b.switchUser.setOnCheckedChangeListener { _,chk -> onState(adm,chk) }
            b.ivEdit.setOnClickListener { onEdit(adm) }
        }
    }
}
