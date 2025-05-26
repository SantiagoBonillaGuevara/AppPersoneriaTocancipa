package com.personeriatocancipa.app.ui.admin.managementAdminPqrs

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.personeriatocancipa.app.R

class ModifyAdminPqrsActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_register)

        val uid = intent.getStringExtra("uid").orEmpty()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainerView,
                ModifyAdminPqrsFragment().apply {
                    arguments = Bundle().apply { putString("uid", uid) }
                }
            )
            .commit()
    }

    fun finishOk() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
