package com.personeriatocancipa.app.domain.validators

import com.personeriatocancipa.app.domain.model.Lawyer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date

class LawyerAvailabilityValidator {

    // Función para verificar las horas disponibles para una cita en la fecha seleccionada
    fun getAvailableHoursForAppointment(lawyer: Lawyer, selectedDate: String, citas: List<com.personeriatocancipa.app.domain.model.Date>): List<String> {
        // Obtener el día de la semana de la fecha seleccionada (lunes = 1, martes = 2, ...)
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate)
        val calendar = Calendar.getInstance().apply { time = date!! }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if(citas.isEmpty()) return getAvailableHoursForDay(lawyer, dayOfWeek).filter { it != "12:00" }
        // Obtener el horario del abogado para el día de la semana correspondiente
        val availableHours = getAvailableHoursForDay(lawyer, dayOfWeek)

        // Filtrar las horas disponibles
        val freeHours = mutableListOf<String>()

        for (hour in availableHours) {
            // Si no hay ninguna cita en esa hora, agregamos a la lista de horas libres
            if (!citas.any { it.hora == hour }) {
                freeHours.add(hour)
            }
        }
        // Retornar las horas libres
        return freeHours
    }

    // Función para obtener las horas disponibles para un día de la semana específico
    private fun getAvailableHoursForDay(lawyer: Lawyer, dayOfWeek: Int): List<String> {
        val availableHours = mutableListOf<String>()
        val startTime: Date?
        val endTime: Date?

        // Obtener el inicio y fin del horario según el día de la semana
        when (dayOfWeek) {
            Calendar.MONDAY -> {
                startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Lunes?.inicio ?: "00:00")
                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Lunes?.fin ?: "00:00")
            }
            Calendar.TUESDAY -> {
                startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Martes?.inicio ?: "00:00")
                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Martes?.fin ?: "00:00")
            }
            Calendar.WEDNESDAY -> {
                startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Miércoles?.inicio ?: "00:00")
                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Miércoles?.fin ?: "00:00")
            }
            Calendar.THURSDAY -> {
                startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Jueves?.inicio ?: "00:00")
                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Jueves?.fin ?: "00:00")
            }
            Calendar.FRIDAY -> {
                startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Viernes?.inicio ?: "00:00")
                endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(lawyer.horario?.Viernes?.fin ?: "00:00")
            }
            else -> return emptyList() // Si no es un día de lunes a viernes, no tiene horario
        }

        // Crear una lista con todas las horas posibles dentro del rango del abogado
        val calendar = Calendar.getInstance()
        calendar.time = startTime

        while (calendar.time.before(endTime)) {
            val hour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            if (hour != "12:00") { // Excluir la hora de almuerzo
                availableHours.add(hour)
            }
            calendar.add(Calendar.HOUR, 1)
        }

        return availableHours
    }
}