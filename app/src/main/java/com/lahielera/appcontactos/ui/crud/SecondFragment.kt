package com.lahielera.appcontactos.ui.crud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lahielera.appcontactos.R
import com.lahielera.appcontactos.databinding.FragmentSecondBinding
import com.lahielera.appcontactos.model.Contacto
import java.util.*

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var viewModel: ContactoViewModel
    private var contacto = Contacto()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_second,
                container,
                false
        )
        binding.guardarContacto.setOnClickListener {
            saveContacto()
        }
        val contactoFragmentArgs by navArgs<SecondFragmentArgs>()
        if (contactoFragmentArgs.contacto != null) {
            contacto = contactoFragmentArgs.contacto!!
            binding.contacto = contacto
        }
//        val contacto = contactoFragmentArgs.contacto
//        binding.contacto = contacto
        binding.setLifecycleOwner(this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactoViewModel::class.java)
        viewModel.onSaveSucess.observe(viewLifecycleOwner, Observer { onSucess ->
            if (onSucess) {
                clearForm()
            }
        })
    }

    private fun clearForm() {
        Toast.makeText(requireContext(), "Contacto guardado", Toast.LENGTH_SHORT).show()
        with(binding) {
            contactoApellido.editText?.text?.clear()
            contactoNombre.editText?.text?.clear()
            contactoCelular.editText?.text?.clear()
            contactoMail.editText?.text?.clear()
            contactoTelefono.editText?.text?.clear()
            contactoApellido.editText?.requestFocus()
        }
    }

    private fun saveContacto() {
        val contacto = Contacto()
        with(binding) {
            contacto.apellidos = contactoApellido.editText?.text
                .toString().capitalize(Locale.getDefault()) ?: "apellidos"
            contacto.nombres =  contactoNombre.editText?.text
                .toString().capitalize(Locale.getDefault()) ?: "nombres"
            contacto.telefono_celular = contactoCelular.editText?.text
                .toString() ?: "123456789"
            contacto.email = contactoMail.editText?.text
                .toString() ?: "asdf@asdf.com"
            contacto.telefono_casa =  contactoTelefono.editText?.text
                .toString() ?: "987654321"
        }
        if (this.contacto.id.isEmpty()) {
            viewModel.saveContacto(contacto)
        } else {
            contacto.id = this.contacto.id
            viewModel.updateContacto(contacto)
        }

    }
}