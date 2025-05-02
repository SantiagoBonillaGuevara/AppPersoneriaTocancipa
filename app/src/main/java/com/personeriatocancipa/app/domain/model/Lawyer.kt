package com.personeriatocancipa.app.domain.model

data class Lawyer (
    override val uid: String? = null, // Llave primaria
    override val documento: String? = null, // Llave primaria
    override val nombreCompleto: String? = null, // Llave primaria
    val cargo: String? = null,
    val tema: String? = null,
    override val correo: String? = null,
    override val estado: String? = null, // Activo, inactivo
) : RegistrableUser{
    override fun withUid(newUid: String): RegistrableUser = this.copy(uid = newUid)
}