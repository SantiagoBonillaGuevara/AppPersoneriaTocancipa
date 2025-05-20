package com.personeriatocancipa.app.ui.common.export

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.print.pdf.PrintedPdfDocument
import android.text.TextPaint
import com.personeriatocancipa.app.domain.model.Pqrs
import java.io.File
import java.io.FileOutputStream

class PdfExporter(private val context: Context) {

    fun export(list: List<Pqrs>, filename: String): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = TextPaint()
        paint.textSize = 12f

        var y = 40f
        list.forEach { pqrs ->
            val line = "${pqrs.id} - ${pqrs.title} (${pqrs.type})"
            canvas.drawText(line, 40f, y, paint)
            y += 20
        }

        document.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
        FileOutputStream(file).use { document.writeTo(it) }

        document.close()
        return file
    }
}
