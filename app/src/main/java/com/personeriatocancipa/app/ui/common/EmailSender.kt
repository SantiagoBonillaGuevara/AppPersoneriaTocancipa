package com.personeriatocancipa.app.ui.common

import android.util.Log
import com.personeriatocancipa.app.domain.model.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender {
    fun sendEmailInBackground(date: Date, subject: String) {
        val body:String = when (subject) {
            "Reagendamiento de Cita" -> "Su cita ha sido reagendada "
            "Agendamiento de Cita" -> "Su cita ha sido agendada "
            else -> "Acción no reconocida."
        }+"para el dia ${date.fecha} a las ${date.hora}."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sendEmail(date.correoAbogado!!, subject, "Estimado abogado, ${body} con el cliente ${date.correoCliente}")
                if(date.autorizaCorreo== "Sí") sendEmail(date.correoCliente!!, subject, "Estimado usuario, ${body} con el abogado ${date.correoAbogado}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun sendEmail(recipientEmail: String, subject: String, body: String) {
        withContext(Dispatchers.IO) {
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = "smtp.gmail.com" // Cambia según tu proveedor de email
            props["mail.smtp.port"] = "587"
            val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                    return javax.mail.PasswordAuthentication("personeriatocancipacol@gmail.com", "fbyl ftub yihk lrlq")
                }
            })
            // https://support.google.com/mail/answer/185833?hl=es-419 -documentacion
            // es importante para crear la key de enviar
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress("personeriatocancipacol@gmail.com"))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                message.subject = subject
                message.setText(body)
                Transport.send(message)
                Log.i( "EmailSender", "Email sent successfully to $recipientEmail")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}