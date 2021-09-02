package com.example.test.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.test.MainActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        btn_pass_app.setOnClickListener {
            checkPasswordApp(et_pass_app.text.toString())
        }
        Log.d("TAG", "onCreate: Password Activity created")
    }

    private fun checkPasswordApp(enterPassword: String){
        val password = getSharedPreferences(
            "com.example.hn21_cpl_android_03g2_preferences",
            Context.MODE_PRIVATE
        ).getString("password_application","")
        if (password.equals("")){
            Toast.makeText(this, "Please setup your password app", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        } else if (password.equals(enterPassword, ignoreCase = true)) {
            Toast.makeText(this, "Correct Password App", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        } else if (!password.equals(enterPassword, ignoreCase = true)){
            Toast.makeText(this, "Wrong Password App", Toast.LENGTH_SHORT).show()
        }
    }
}