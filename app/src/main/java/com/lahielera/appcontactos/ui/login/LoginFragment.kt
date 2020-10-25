package com.lahielera.appcontactos.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.appcontactos.LoginActivity
import com.lahielera.appcontactos.R
import com.lahielera.appcontactos.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        with(binding) {
            botonLogin.setOnClickListener {
                signIn(usuarioLogin.text.toString()
                    , passwordLogin.text.toString())
            }

            botonRegistrarse.setOnClickListener {
                moveToRegistro()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity().applicationContext, gso)
        auth = Firebase.auth
    }

    private fun moveToRegistro(){
       findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavRegistro())
    }

    private fun signIn(email: String, password: String) {
        if (!validarForm()) {
            return
        }
        //TODO showProgressBar
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Bienvenido!", Toast.LENGTH_SHORT).show()
                    (activity as LoginActivity).moveToMain()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Correo o password no validos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //TODO hideProgressBar
            }
    }

    private fun validarForm(): Boolean{
        var valido = true
        val email = binding.usuarioLogin.text.toString()
        if (TextUtils.isEmpty(email) || !validarEmail(email)) {
            binding.usuarioLayout.error = "Ingrese un correo electronico"
            valido = false
        } else {
            binding.usuarioLayout.error = null
        }

        val password = binding.passwordLogin.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordLayout.error = "Ingrese una contraseña"
            binding.passwordLogin.hasFocus()
            valido = false
        } else {
            binding.passwordLayout.error = null
        }
        return valido
    }

    private fun validarEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}