package com.personeriatocancipa.app

class Date {
    var id: Int? = null // Autonumérico
    var descripcion: String? = null
    var fecha: String? = null
    var hora: String? = null
    var correoAbogado: String? = null
    var correoCliente: String? = null // A dónde se notifica
    var tema: String? = null // Víctimas, Administrativo, etc.
    var autorizaCorreo: String? = null // Sí, No
    var correoVigente: String? = null // Sí, No
    var estado: String? = null // Pendiente, cancelada, realizada, etc.

    constructor()

    constructor(
        id: Int?,
        descripcion: String?,
        fecha: String?,
        hora: String?,
        correoAbogado: String?,
        correoCliente: String?,
        tema: String?,
        autorizaCorreo: String?,
        correoVigente: String?,
        estado: String?
    ) {
        this.id = id
        this.descripcion = descripcion
        this.fecha = fecha
        this.hora = hora
        this.correoAbogado = correoAbogado
        this.correoCliente = correoCliente
        this.tema = tema
        this.autorizaCorreo = autorizaCorreo
        this.correoVigente = correoVigente
        this.estado = estado
    }

}