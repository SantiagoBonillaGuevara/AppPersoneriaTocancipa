package com.personeriatocancipa.app.ui.admin.managementDates.editDate

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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
import com.personeriatocancipa.app.ui.admin.managementDates.editDate.fragments.EditDateStep1Fragment
import com.personeriatocancipa.app.ui.common.EmailSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditDateActivity : AppCompatActivity() {
    private val viewModel: EditDateViewModel by viewModels()
    private val getAllUsersUseCase = GetAllUsersUseCase(FirebaseUserRepository())
    private val saveDateUseCase = CreateDateUseCase(FirebaseDateRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val dateId = intent.getStringExtra("id")
        viewModel.loadCurrentDate(dateId!!.toInt())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, EditDateStep1Fragment())
            .commit()
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
                Toast.makeText(this@EditDateActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isAvailableOnDay(lawyer: Lawyer, fecha: String): Boolean {
        // Convertir la fecha en un día de la semana
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fecha)
        val calendar = Calendar.getInstance().apply { time = date!! }
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

    fun saveDate(){
        lifecycleScope.launch {
            viewModel.date.value?.let { date ->
                val result = saveDateUseCase.execute(date.id!! ,date)
                result.onSuccess {
                    Toast.makeText(this@EditDateActivity, "Cita re agendada exitosamente", Toast.LENGTH_SHORT).show()
                    sendEmailInBackground(date, "Reagendamiento de Cita")
                    setResult(Activity.RESULT_OK)
                    finish()
                }.onFailure { error ->
                    Toast.makeText(this@EditDateActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
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