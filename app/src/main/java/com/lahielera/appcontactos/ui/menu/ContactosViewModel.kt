package com.lahielera.appcontactos.ui.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.appcontactos.model.Contacto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ContactosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var contactos = MutableLiveData<ArrayList<Contacto>>()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid.toString()

    private val _onSucess = MutableLiveData<Boolean>()
    val onSucess: LiveData<Boolean>
        get() = _onSucess

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        getContactosFromFirestore()
        _onSucess.value = false
    }

    fun updateContactos () {
        getContactosFromFirestore()
    }

    private fun getContactosFromFirestore() {
        Log.d("ViewModel", "getContactos")
        var data = arrayListOf<Contacto>()
        db.collection("Usuarios").document(userId).collection("Contactos")
            .orderBy("apellidos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val contacto = document.toObject(Contacto::class.java)
                    Log.d("Contacto", contacto.id)
                    data.add(contacto)
                }
                contactos.value = data
            }
            .addOnFailureListener { exception ->
                Log.e("ContactosVM", "Error: ", exception)
            }
    }

    fun deleteContacto (contactoId: String) {
        db.collection("Usuarios").document(userId).collection("Contactos").document(contactoId)
                .delete()
                .addOnCompleteListener { task ->
                    _onSucess.value = true
                }
        //_onSucess.value = false
    }

    fun resetOnSuccessEvent() {
        _onSucess.value = false
    }
}