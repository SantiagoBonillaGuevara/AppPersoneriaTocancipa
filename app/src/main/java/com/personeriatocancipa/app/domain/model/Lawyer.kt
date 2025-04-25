package com.personeriatocancipa.app.domain.model

data class Lawyer (
    var documento: String? = null, // Llave primaria
    var nombreCompleto: String? = null, // Llave primaria
    var cargo: String? = null,
    var tema: String? = null,
    var correo: String? = null,
    var estado: String? = null, // Activo, inactivo
)