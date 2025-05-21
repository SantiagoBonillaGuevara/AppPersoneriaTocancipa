package com.personeriatocancipa.app.domain.model

/**
 * Representa una petición/queja/reclamo/sugerencia (PQRS).
 */
data class Pqrs(
    val id: String = "",
    val userId: String = "",
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val attachment: String? = null,
    val response: String? = null,
    val date: Long = 0L,
    val status: String = ""
)
