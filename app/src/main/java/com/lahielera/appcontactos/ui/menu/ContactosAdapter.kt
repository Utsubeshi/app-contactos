package com.lahielera.appcontactos.ui.menu

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.appcontactos.R
import com.lahielera.appcontactos.databinding.ItemContactoBinding
import com.lahielera.appcontactos.model.Contacto

class ContactosAdapter (): RecyclerView.Adapter<ContactosAdapter.ViewHolder>() {

    private var data: ArrayList<Contacto> = arrayListOf<Contacto>()
    private lateinit var listener: OnContactoCLickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactosAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactosAdapter.ViewHolder, position: Int) {
        holder.binding.contacto = data[position]
        holder.binding.nombreContacto.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data[position].telefono_celular))
            it.context.startActivity(intent)
        }
        holder.binding.btnEliminar.setOnClickListener {
            listener.borrarContacto(data[position].id)
            data.remove(data[position])
            notifyDataSetChanged()
        }
        holder.binding.btnEdit.setOnClickListener {
            listener.verContacto(data[position])
        }
    }

    override fun getItemCount() = data.size

    fun addData(listaContactos: ArrayList<Contacto>, listener: OnContactoCLickListener) {
        this.data = listaContactos
        this.listener = listener
        notifyDataSetChanged()
    }

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)  {
        val binding = ItemContactoBinding.bind(itemView)
    }

    interface OnContactoCLickListener {
        fun borrarContacto(uid: String)

        fun verContacto(contacto: Contacto)
    }
}