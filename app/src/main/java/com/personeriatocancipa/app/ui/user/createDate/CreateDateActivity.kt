package com.personeriatocancipa.app.ui.user.createDate

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityRegisterBinding
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.usecase.CreateDateUseCase
import com.personeriatocancipa.app.domain.usecase.GetAllUsersUseCase
import com.personeriatocancipa.app.domain.usecase.GetDatesUseCase
import com.personeriatocancipa.app.ui.common.EmailSender
import com.personeriatocancipa.app.ui.user.createDate.fragments.CreateDateStep1Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CreateDateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: CreateDateViewModel by viewModels()
    private val getAllUsersUseCase = GetAllUsersUseCase(FirebaseUserRepository())
    private val createDateUseCase = CreateDateUseCase(FirebaseDateRepository())
    private val getDatesUseCase = GetDatesUseCase(FirebaseDateRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        confirmEmail()
        viewModel.loadCurrentUser()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, CreateDateStep1Fragment())
            .commit()
    }

    private fun confirmEmail() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Envío de confirmación a Correo Electrónico")
        builder.setMessage("¿Está de acuerdo con que se le envíe respuesta o información sobre la gestión de su PETICIÓN vía correo electrónico?")
        builder.setPositiveButton("Sí") { dialog, which ->
            viewModel.date.value?.autorizaCorreo = "Sí"
        }
        builder.setNegativeButton("No") { dialog, which ->
            viewModel.date.value?.autorizaCorreo = "No"
        }
        builder.create().show()

        // Preguntar si el correo es vigente
        builder = AlertDialog.Builder(this)
        builder.setTitle("Vigencia de correo electrónico")
        builder.setMessage("Certifico que el correo electrónico ingresado se encuentra vigente, y se autoriza a la Personería de Tocancipá para que realice notificaciones electrónicas a través de este medio de las actuaciones realizadas por la entidad, en los términos del artículo 56 de la Ley 1437 de 2011, y las normas que la modifiquen, aclaren o sustituyan")
        builder.setPositiveButton("Sí") { dialog, which ->
            viewModel.date.value?.correoVigente = "Sí"
        }
        builder.setNegativeButton("No") { dialog, which ->
            viewModel.date.value?.correoVigente = "No"
        }
        builder.create().show()
    }

    fun navigateToNextStep(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun getLawyers(tema:String, fecha:String) {
        lifecycleScope.launch {
            val result = getAllUsersUseCase.execute("abogadoData")
            result.onSuccess { lawyerList ->
                // Filtrar por tema y disponibilidad según el día de la semana
                val filteredLawyers = (lawyerList as List<Lawyer>).filter { lawyer ->
                    lawyer.temas?.contains(tema) == true && isAvailableOnDay(lawyer, fecha) && lawyer.estado=="Activo"
                }
                viewModel.lawyerList.value = filteredLawyers
            }.onFailure { error ->
                Toast.makeText(this@CreateDateActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isAvailableOnDay(lawyer: Lawyer, fecha: String): Boolean {
        // Convertir la fecha en un día de la semana
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fecha)
        val calendar = Calendar.getInstance().apply { time = date }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // Verificar si el abogado tiene horario para ese día
        return when (dayOfWeek) {
            Calendar.MONDAY -> lawyer.horario?.Lunes?.inicio.isNullOrEmpty().not()
            Calendar.TUESDAY -> lawyer.horario?.Martes?.inicio.isNullOrEmpty().not()
            Calendar.WEDNESDAY -> lawyer.horario?.Miércoles?.inicio.isNullOrEmpty().not()
            Calendar.THURSDAY -> lawyer.horario?.Jueves?.inicio.isNullOrEmpty().not()
            Calendar.FRIDAY -> lawyer.horario?.Viernes?.inicio.isNullOrEmpty().not()
            else -> false
        }
    }

    fun createDate() {
        lifecycleScope.launch {
            viewModel.date.value?.let { date ->
                val resultId = getDatesUseCase.execute("", "")
                resultId.onSuccess {
                    val id = it.size + 1
                    val result = createDateUseCase.execute(id ,date)
                    result.onSuccess {
                        Toast.makeText(this@CreateDateActivity, "Cita creada exitosamente", Toast.LENGTH_SHORT).show()
                        sendEmailInBackground(date, "Agendamiento de Cita")
                        finish()
                    }.onFailure { error ->
                        Toast.makeText(this@CreateDateActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun sendEmailInBackground(date: Date, subject: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                EmailSender().sendEmailInBackground(date, subject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}