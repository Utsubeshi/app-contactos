package com.lahielera.appcontactos.ui.crud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.appcontactos.model.Contacto

class ContactoViewModel() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid.toString()
    private val contactosRef = db.collection("Usuarios").document(userId).collection("Contactos")

    private var _onSaveSucess = MutableLiveData<Boolean>()
    val onSaveSucess : LiveData<Boolean>
        get() = _onSaveSucess

    init {

    }

    fun saveContacto(contacto: Contacto) {
        contactosRef.add(contacto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _onSaveSucess.value = true
            }
        }
        _onSaveSucess.value = false
    }

    fun  updateContacto(contacto: Contacto) {
        contactosRef.document(contacto.id).set(contacto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _onSaveSucess.value = true
            }
        }
        _onSaveSucess.value = false
    }
}