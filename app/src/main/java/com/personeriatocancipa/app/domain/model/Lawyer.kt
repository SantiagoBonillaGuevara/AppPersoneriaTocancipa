package com.personeriatocancipa.app.domain.model

data class Lawyer (
    override val uid: String? = null, // Llave primaria
    override val documento: String? = null, // Llave primaria
    override val nombreCompleto: String? = null, // Llave primaria
    val cargo: String? = null,
    val temas: List<String>? = null,
    val horario: Horario? = null,
    override val correo: String? = null,
    override var estado: String? = null, // Activo, inactivo
) : RegistrableUser{
    override fun withUid(newUid: String): RegistrableUser = this.copy(uid = newUid)
}

data class Horario(
    val Lunes: HorarioDia? = null,
    val Martes: HorarioDia? = null,
    val Miércoles: HorarioDia? = null,
    val Jueves: HorarioDia? = null,
    val Viernes: HorarioDia? = null
)

data class HorarioDia(
    val inicio: String? = null,
    val fin: String? = null
)