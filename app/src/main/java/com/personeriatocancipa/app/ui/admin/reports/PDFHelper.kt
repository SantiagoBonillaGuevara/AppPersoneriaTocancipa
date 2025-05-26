package com.personeriatocancipa.app.ui.admin.reports

import android.graphics.Bitmap
import android.os.Environment
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.personeriatocancipa.app.domain.model.Date
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PDFHelper {

    fun exportToPDF(dates: List<Date>, barChartBitmap: Bitmap?, pieChartBitmap: Bitmap?, lineChartBitmap: Bitmap?):String {
        try {
            val pdfDoc = Document(PageSize.A4.rotate()) // Establecer la página en formato A4 y orientación horizontal
            val fileName = "Reporte_Citas_${System.currentTimeMillis()}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            PdfWriter.getInstance(pdfDoc, FileOutputStream(file))
            pdfDoc.open()

            // Título del PDF
            val title = Paragraph("REPORTE DE CITAS",
                Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLUE))
            title.alignment = Element.ALIGN_CENTER
            pdfDoc.add(title)

            // Subtítulo con la fecha del reporte
            val subtitle = Paragraph("Fecha: ${java.util.Calendar.getInstance().time}",
                Font(Font.FontFamily.HELVETICA, 12f))
            subtitle.alignment = Element.ALIGN_CENTER
            pdfDoc.add(subtitle)

            // Resumen estadístico
            val totalCitas = dates.size
            val summary = Paragraph().apply {
                add(Phrase("Total citas: ", Font(Font.FontFamily.HELVETICA, 12f)))
                add(Phrase("$totalCitas\n", Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)))
            }
            pdfDoc.add(summary)

            // Tabla de datos
            val table = PdfPTable(4)
            table.widthPercentage = 100f
            table.setWidths(floatArrayOf(3f, 2f, 2f, 2f))

            // Encabezados de la tabla
            val headers = listOf("Abogado", "Fecha", "Hora", "Estado")
            headers.forEach { header ->
                val cell = PdfPCell(Phrase(header,
                    Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.WHITE)))
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.backgroundColor = BaseColor(79, 129, 189)
                cell.borderWidth = 1f
                table.addCell(cell)
            }

            // Insertar los datos en la tabla
            dates.forEach { date ->
                table.addCell(createTableCell(date.correoAbogado ?: ""))
                table.addCell(createTableCell(date.fecha ?: ""))
                table.addCell(createTableCell(date.hora ?: ""))
                table.addCell(createTableCell(date.estado ?: ""))
            }

            pdfDoc.add(table)

            // Agregar la gráfica de pastel (si está disponible)
            pieChartBitmap?.let { bitmap ->
                addChartImageToPDF(pdfDoc, bitmap)
            }

            // Agregar la gráfica de barras (si está disponible)
            barChartBitmap?.let { bitmap ->
                addChartImageToPDF(pdfDoc, bitmap)
            }

            // Agregar la gráfica de líneas (si está disponible)
            lineChartBitmap?.let { bitmap ->
                addChartImageToPDF(pdfDoc, bitmap)
            }

            // Cerrar el PDF
            pdfDoc.close()

            return "PDF guardado en: $fileName"
        } catch (e: Exception) {
            return "Error al generar PDF: ${e.message}"
        }
    }

    // Función para crear celdas de la tabla
    private fun createTableCell(text: String): PdfPCell {
        return PdfPCell(Phrase(text, Font(Font.FontFamily.HELVETICA, 10f))).apply {
            horizontalAlignment = Element.ALIGN_CENTER
            setPadding(5f)
        }
    }

    // Función para agregar la imagen del gráfico al PDF
    private fun addChartImageToPDF(pdfDoc: Document, bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        try {
            val chartImage = Image.getInstance(byteArray)
            chartImage.scaleToFit(PageSize.A4.width, PageSize.A4.height)
            pdfDoc.add(chartImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

