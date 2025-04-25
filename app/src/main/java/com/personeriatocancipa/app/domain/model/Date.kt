package com.personeriatocancipa.app.domain.model

data class Date (
    var id: Int? = null, // Autonumérico
    var descripcion: String? = null,
    var fecha: String? = null,
    var hora: String? = null,
    var correoAbogado: String? = null,
    var correoCliente: String? = null, // A dónde se notifica
    var tema: String? = null, // Víctimas, Administrativo, etc.
    var autorizaCorreo: String? = null, // Sí, No
    var correoVigente: String? = null, // Sí, No
    var estado: String? = null // Pendiente, cancelada, realizada, etc.
)