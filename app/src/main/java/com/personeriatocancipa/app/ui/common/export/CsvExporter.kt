package com.personeriatocancipa.app.ui.common.export

import android.content.Context
import android.os.Environment
import com.personeriatocancipa.app.domain.model.Pqrs
import java.io.File
import java.io.FileOutputStream

class CsvExporter(private val context: Context) {

    fun export(list: List<Pqrs>, filename: String): File {
        val csv = StringBuilder()
        csv.append("ID,Título,Tipo,Descripción,Fecha\n")
        list.forEach { pqrs ->
            csv.append("${pqrs.id},\"${pqrs.title}\",${pqrs.type},\"${pqrs.description}\",${pqrs.date}\n")
        }

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
        FileOutputStream(file).use { it.write(csv.toString().toByteArray()) }

        return file
    }
}
