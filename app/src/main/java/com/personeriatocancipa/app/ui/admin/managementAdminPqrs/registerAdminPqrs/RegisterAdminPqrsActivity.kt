package com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.ActivityRegisterBinding
import com.personeriatocancipa.app.ui.admin.managementAdminPqrs.registerAdminPqrs.fragments.RegisterAdminPqrsStep1Fragment

class RegisterAdminPqrsActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, RegisterAdminPqrsStep1Fragment())
            .commit()
    }

    fun navigateNext(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun finishOk() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
