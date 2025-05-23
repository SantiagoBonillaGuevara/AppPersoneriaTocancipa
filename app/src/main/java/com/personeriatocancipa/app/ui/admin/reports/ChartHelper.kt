package com.personeriatocancipa.app.ui.admin.reports

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.personeriatocancipa.app.domain.model.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartHelper(private val barChart: BarChart, private val pieChart: PieChart, private val citasChart: BarChart) {

    fun setupBarChart(dates: List<Date>) {
        // Aquí va tu lógica para crear el BarChart
        val themeCount = dates.groupingBy { it.tema }.eachCount()

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        themeCount.entries.forEachIndexed { index, entry ->
            entries.add(BarEntry(index.toFloat(), entry.value.toFloat())) // Y: Cantidad de citas
            labels.add(entry.key ?: "Sin tema") // X: Tema de la cita
        }

        val dataSet = BarDataSet(entries, "Número de citas por tema")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)
        barChart.data = data

        // Configuración visual
        barChart.setFitBars(true)
        barChart.description.text = "Citas por Tema"
        barChart.animateY(1000)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }

    fun setupLineChart(dates: List<Date>) {
        val dayCount = mutableMapOf<String, Int>()
        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

        // Contar las citas por día de la semana
        dates.forEach { date ->
            val dayOfWeek = getDayOfWeek(date.fecha!!)  // Asegúrate de que getDayOfWeek devuelva nombres exactos como en daysOfWeek
            dayCount[dayOfWeek] = dayCount.getOrDefault(dayOfWeek, 0) + 1
        }

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        daysOfWeek.forEachIndexed { index, day ->
            val count = dayCount[day] ?: 0
            entries.add(BarEntry(index.toFloat(), count.toFloat()))
            labels.add(day)
        }

        val dataSet = BarDataSet(entries, "Número de citas por día")
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS.toList()) // Colores variados para cada barra
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)
        citasChart.data = data

        // Configuración visual
        citasChart.setFitBars(true)
        citasChart.description.text = "Citas por día de la semana"
        citasChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        citasChart.xAxis.granularity = 1f
        citasChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        citasChart.animateY(1000)
        citasChart.invalidate()
    }

    fun setupPieChart(dates: List<Date>) {
        val statusCount = dates.groupingBy { it.estado }.eachCount()

        val entries = ArrayList<PieEntry>()
        statusCount.entries.forEach { entry ->
            val percentage = (entry.value.toFloat() / dates.size) * 100
            entries.add(PieEntry(percentage, entry.key ?: "Sin estado"))
        }

        val dataSet = PieDataSet(entries, "Distribución de citas por estado")
        dataSet.colors = getVividColors(statusCount.size)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart.data = data

        // Configuración visual
        pieChart.description.text = "Porcentaje de citas por estado"
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun getVividColors(count: Int): List<Int> {
        val colors = ArrayList<Int>()
        val hueStep = 360f / count

        for (i in 0 until count) {
            val hue = i * hueStep
            val color = Color.HSVToColor(floatArrayOf(
                hue, 1f, 1f
            ))
            colors.add(color)
        }

        return colors
    }

    private fun getDayOfWeek(date: String): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            else -> "Sin día"
        }
    }
}
