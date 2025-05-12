package com.personeriatocancipa.app.ui.user

//import com.personeriatocancipa.app.GetUserDatesActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.data.repository.FirebaseUserRepository
import com.personeriatocancipa.app.databinding.ActivityUserBinding
import com.personeriatocancipa.app.domain.model.User
import com.personeriatocancipa.app.domain.usecase.GetUserUseCase
import com.personeriatocancipa.app.ui.chat.ChatBotActivity
import com.personeriatocancipa.app.ui.common.LoginActivity
import com.personeriatocancipa.app.ui.user.createDate.CreateDateActivity
import com.personeriatocancipa.app.ui.user.getDates.GetUserDatesActivity
import com.personeriatocancipa.app.ui.user.modify.ModifyUserActivity
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepository())
    private lateinit var user: User
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initComponents()
    }

    private fun initUI(){
        cargarNombre()
    }

    private fun initComponents(){
        binding.flDate.setOnClickListener{
            navigateToCreateDate()
        }

        binding.flSearch.setOnClickListener{
            navigateToGetUserDates()
        }

        binding.flEdit.setOnClickListener{
            navigateToModify()
        }

        binding.flChat.setOnClickListener{
            navigateToPQRS()
        }

        binding.flExit.setOnClickListener{
            navigateToLogin()
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) cargarNombre()
        }
    }

    private fun cargarNombre() {
        lifecycleScope.launch {
            val result = getUserUseCase.execute("userData","")
            result.onSuccess {
                if (it is User) {
                    user = it
                    binding.tvName.text = user.nombreCompleto
                }
            }
        }
    }

    private fun navigateToCreateDate(){
        val intent = Intent(this@UserActivity, CreateDateActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToGetUserDates(){
        val intent = Intent(this@UserActivity, GetUserDatesActivity::class.java)
        intent.putExtra("user", user.correo)
        startActivity(intent)
    }

    private fun navigateToModify(){
        val intent = Intent(this@UserActivity, ModifyUserActivity::class.java)
        launcher.launch(intent)
    }

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun navigateToPQRS(){
        val intent = Intent(this,ChatBotActivity::class.java)
        startActivity(intent)
    }
}