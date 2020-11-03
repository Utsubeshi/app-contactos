package com.lahielera.appcontactos.ui.registro

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.appcontactos.LoginActivity
import com.lahielera.appcontactos.R
import com.lahielera.appcontactos.databinding.RegistroFragmentBinding
import java.util.*

class RegistroFragment : Fragment() {

    private lateinit var binding: RegistroFragmentBinding
    private lateinit var viewModel: RegistroViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registro_fragment,
            container,
            false
        )
        binding.botonRegistro.setOnClickListener {
            crearCuenta(binding.mailLayout.editText?.text.toString()
                , binding.passLayout.editText?.text.toString()
                , binding.nombresLayout.editText?.text.toString().capitalize(Locale.getDefault())
                , binding.apellidosLayout.editText?.text.toString().capitalize(Locale.getDefault()))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistroViewModel::class.java)
        viewModel.registroCompleto.observe(viewLifecycleOwner, Observer { registroCompletado ->
            if (registroCompletado) moveToLogin()
        })
        auth = Firebase.auth
    }

    private fun crearCuenta(mail: String, pass: String, nombres: String, apellidos: String){

        if (!validarFormulario()) return

        auth.createUserWithEmailAndPassword(mail, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Usuarios registrado, bienvendo!",
                        Toast.LENGTH_SHORT
                    ).show()
                    (activity as LoginActivity).moveToMain()
                    viewModel.saveUserData(nombres, apellidos, auth.currentUser?.uid, mail)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Se ha producido un error, intentelo nuevamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validarFormulario(): Boolean {
        //TODO debo validar los campos vacios algun dia D:!
        return true
    }

    private fun moveToLogin(){
        findNavController().navigate(RegistroFragmentDirections.actionNavRegistroToNavLogin2())
    }

}