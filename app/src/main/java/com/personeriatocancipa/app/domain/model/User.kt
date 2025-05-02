package com.personeriatocancipa.app.domain.model

data class User (
    override val nombreCompleto: String? = null,
    val tipoDocumento: String? = null, // CC, CE, NIT, Pasaporte, PEP, PTP, RC, TI
    override val documento: String? = null,
    val fechaNacimiento: String? = null, // dd-mm-yyyy
    val grupoEtario: String? = null, // Primera infancia, infancia, adolescencia, juventud, adultez, persona mayor
    val edad: Int? = null,
    val direccion: String? = null,
    val sector: String? = null, // Betania, Bohio, La Aurora...
    val telefono: String? = null,
    override val correo: String? = null,
    val sexo: String? = null, // Hombre, Mujer, Intersexual
    val identidad: String? = null, // Femenino, Masculino, Transgénero, No deseo informar
    val orientacion: String? = null, // Bisexual, Heterosexual, Homosexual, No deseo informar
    val nacionalidad: String? = null, // Femenino, Masculino, Transgénero, No deseo informar
    val escolaridad: String? = null, // Primaria, Secundaria, Técnico, Profesional...
    val grupoEtnico: String? = null, // Afrocolombiano, indígena, ROM, raizal, palenquero, gitano, no pertenece a ninguno
    val discapacidad: String? = null, // Auditiva, física, intelectual, visual, sordoceguera, psicosocial, múltiple, ninguna
    val estrato: String? = null, // 1, 2, 3,..., No informa
    val comunidad: String? = null, // Madre Cabeza de Familia, Víctima del conflicto armado, Discapacidad, Adulto Mayor, LGBTIQ+, Ninguna, Otros
    override val estado: String? = null, // Activo, inactivo
    override val uid: String? = null // Llave primaria
) : RegistrableUser{
    override fun withUid(newUid: String): RegistrableUser = this.copy(uid = newUid)
}