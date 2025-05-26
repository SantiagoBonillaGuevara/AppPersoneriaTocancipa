package com.personeriatocancipa.app.domain.validators

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateValidator {

    private val festivosFijos = setOf(
        1 to 1,    // Año Nuevo
        1 to 6,    // Reyes Magos
        3 to 24,   // Domingo de Ramos
        4 to 14,   // Jueves Santo
        4 to 15,   // Viernes Santo
        5 to 1,    // Día del Trabajo
        5 to 22,   // Ascensión del Señor
        6 to 12,   // Corpus Christi
        6 to 19,   // Sagrado Corazón
        7 to 20,   // Día de la Independencia
        8 to 7,    // Batalla de Boyacá
        8 to 21,   // Asunción de la Virgen
        10 to 16,  // Día de la Raza
        11 to 6,   // Todos los Santos
        11 to 13,  // Independencia de Cartagena
        12 to 8,   // Inmaculada Concepción
        12 to 25   // Navidad
    )

    fun esFechaAnticipada(fecha: String, diasMinimos: Int = 7): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val fechaSeleccionada = formato.parse(fecha)
            val limite = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, diasMinimos)
            }
            fechaSeleccionada?.after(limite.time) == true
        } catch (e: ParseException) {
            false
        }
    }

    fun esFestivo(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val fechaSeleccionada = formato.parse(fecha)
            val calendar = Calendar.getInstance().apply {
                time = fechaSeleccionada!!
            }

            val mes = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH es base 0
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            !festivosFijos.contains(mes to dia)
        } catch (e: ParseException) {
            false
        }
    }
}