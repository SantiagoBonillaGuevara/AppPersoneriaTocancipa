package com.personeriatocancipa.app.domain.model

data class Admin(
    override val uid: String? = null, // Llave primaria
    override val documento: String? = null, // LLave primaria
    override val nombreCompleto: String? = null, // LLave primaria
    override val correo: String? = null,
    override var estado: String? = null, // Activo, inactivo
) : RegistrableUser{
    override fun withUid(newUid: String): RegistrableUser = this.copy(uid = newUid)
}