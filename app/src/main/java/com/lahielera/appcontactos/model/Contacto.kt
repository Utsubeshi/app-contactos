package com.lahielera.appcontactos.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Contacto  (
        @DocumentId
        var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var email: String = "",
    var telefono_celular: String = "",
    var telefono_casa: String = ""
) : Serializable{

    @Exclude
    fun getNombreCompleto(): String {
        return "${this.apellidos} ${this.nombres}"
    }

}
