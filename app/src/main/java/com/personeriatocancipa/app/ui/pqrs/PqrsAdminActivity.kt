package com.personeriatocancipa.app.ui.pqrs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ActivityPqrsAdminBinding

class PqrsAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPqrsAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPqrsAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_admin) as NavHostFragment
        val navController = host.navController

        // Inflamos el mismo nav_pqrs.xml...
        val graph: NavGraph = navController.navInflater.inflate(R.navigation.nav_pqrs)
        // ...y forzamos que empiece en el listado admin
        graph.setStartDestination(R.id.pqrsAdminListFragment)

        navController.graph = graph
    }
}
