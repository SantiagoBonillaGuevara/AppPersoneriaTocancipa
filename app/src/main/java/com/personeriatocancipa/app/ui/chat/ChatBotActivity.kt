package com.personeriatocancipa.app.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ActivityChatbotBinding

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos ViewBinding
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos la barra (si tienes un Toolbar en el layout)
        setSupportActionBar(binding.toolbar)

        // Obtenemos el NavController del NavHostFragment
        val navController = findNavController(R.id.navHostPqrs)
        // Sincronizamos el botón “Up” con el NavController
        setupActionBarWithNavController(navController)
    }

    // Para que funcione el botón “Up” de la barra
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostPqrs).navigateUp() || super.onSupportNavigateUp()
    }
}
