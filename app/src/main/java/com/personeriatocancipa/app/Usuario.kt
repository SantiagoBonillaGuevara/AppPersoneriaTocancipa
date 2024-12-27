package com.personeriatocancipa.app

class Usuario {

    var nombreCompleto: String? = null
    var tipoDocumento: String? = null // CC, CE, NIT, Pasaporte, PEP, PTP, RC, TI
    var documento: String? = null
    var fechaNacimiento: String? = null // dd-mm-yyyy
    var grupoEtario: String? =
        null // Primera infancia, infancia, adolescencia, juventud, adultez, persona mayor
    var edad: Int? = null
    var direccion: String? = null
    var sector: String? = null // Betania, Bohio, La Aurora...
    var telefono: String? = null
    var correo: String? = null
    var sexo: String? = null // Hombre, Mujer, Intersexual
    var identidad: String? = null // Femenino, Masculino, Transgénero, No deseo informar
    var orientacion: String? = null // Bisexual, Heterosexual, Homosexual, No deseo informar
    var nacionalidad: String? = null // Femenino, Masculino, Transgénero, No deseo informar
    var escolaridad: String? = null // Primaria, Secundaria, Técnico, Profesional...
    var grupoEtnico: String? =
        null // Afrocolombiano, indígena, ROM, raizal, palenquero, gitano, no pertenece a ninguno
    var discapacidad: String? =
        null // Auditiva, física, intelectual, visual, sordoceguera, psicosocial, múltiple, ninguna
    var estrato: String? = null // 1, 2, 3,..., No informa
    var comunidad: String? =
        null // Madre Cabeza de Familia, Víctima del conflicto armado, Discapacidad, Adulto Mayor, LGBTIQ+, Ninguna, Otros
    var estado: String? = null // Activo, inactivo
    var uid: String? = null


    constructor() {

    }

    constructor(
        nombreCompleto: String?,
        tipoDocumento: String?,
        documento: String?,
        fechaNacimiento: String?,
        grupoEtario: String?,
        edad: Int?,
        direccion: String?,
        sector: String?,
        telefono: String?,
        correo: String?,
        sexo: String?,
        identidad: String?,
        orientacion: String?,
        nacionalidad: String?,
        escolaridad: String?,
        grupoEtnico: String?,
        discapacidad: String?,
        estrato: String?,
        comunidad: String?,
        estado: String?,
        uid: String?
    ) {
        this.nombreCompleto = nombreCompleto
        this.tipoDocumento = tipoDocumento
        this.documento = documento
        this.fechaNacimiento = fechaNacimiento
        this.grupoEtario = grupoEtario
        this.edad = edad
        this.direccion = direccion
        this.sector = sector
        this.telefono = telefono
        this.correo = correo
        this.sexo = sexo
        this.identidad = identidad
        this.orientacion = orientacion
        this.nacionalidad = nacionalidad
        this.escolaridad = escolaridad
        this.grupoEtnico = grupoEtnico
        this.discapacidad = discapacidad
        this.estrato = estrato
        this.comunidad = comunidad
        this.estado = estado
        this.uid = uid
    }


}