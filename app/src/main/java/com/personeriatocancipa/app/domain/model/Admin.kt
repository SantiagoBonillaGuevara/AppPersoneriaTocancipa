package com.personeriatocancipa.app.domain.model

data class Admin(
    var cedula: String, // LLave primaria
    var nombreCompleto: String, // LLave primaria
    var correo: String,
    var estado: String, // Activo, inactivo
)