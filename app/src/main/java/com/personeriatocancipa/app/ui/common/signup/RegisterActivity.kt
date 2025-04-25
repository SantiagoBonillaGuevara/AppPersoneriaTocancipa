package com.personeriatocancipa.app.ui.common.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.ui.common.signup.fragments.RegisterStep1Fragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, RegisterStep1Fragment())
            .commit()
    }

    fun navigateToNextStep(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}