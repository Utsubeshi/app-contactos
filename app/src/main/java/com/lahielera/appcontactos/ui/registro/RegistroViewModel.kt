package com.lahielera.appcontactos.ui.registro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.appcontactos.model.Usuario

class RegistroViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var registroCompleto = MutableLiveData<Boolean>()


    init {
        registroCompleto.value = false
    }

    fun saveUserData(nombres: String, apellidos: String, uid: String?, mail: String){
        val usuario = Usuario(nombres, apellidos, mail)
        db.collection("Usuarios").document(uid!!).set(usuario).addOnSuccessListener {
            registroCompleto.value = true
        }
    }
}