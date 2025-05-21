package com.personeriatocancipa.app.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.personeriatocancipa.app.R

class PqrsAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pqrs_admin)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_admin) as NavHostFragment

        val navController: NavController = navHostFragment.navController

        // Cast explícito a NavGraph
        val graph = navController.navInflater.inflate(R.navigation.nav_pqrs) as NavGraph
        graph.setStartDestination(R.id.pqrsAdminListFragment)

        navController.graph = graph
    }
}
