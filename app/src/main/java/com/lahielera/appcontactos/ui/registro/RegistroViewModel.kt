package com.lahielera.appcontactos.ui.registro

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.appcontactos.model.Usuario

class RegistroViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun saveUserData(nombres: String, apellidos: String, uid: String?){
        val usuario = Usuario(nombres, apellidos)
        //db.collection("Usuarios").add(usuario)
        db.collection("Usuarios").document(uid!!).set(usuario)
    }
}