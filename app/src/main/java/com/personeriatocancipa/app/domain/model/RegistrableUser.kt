package com.personeriatocancipa.app.domain.model

interface RegistrableUser {
    val uid: String?
    val correo: String?
    var estado: String?
    val nombreCompleto: String?
    val documento: String?
    fun withUid(newUid: String): RegistrableUser
}