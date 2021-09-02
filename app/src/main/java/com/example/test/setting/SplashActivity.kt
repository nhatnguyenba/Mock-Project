package com.example.test.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.test.MainActivity
import com.example.test.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            checkEnableDarkMode()
            when (getSharedPreferences(
                "com.example.hn21_cpl_android_03g2_preferences",
                Context.MODE_PRIVATE
            ).getBoolean("enable_password_application", true)){
                true -> {
                    delay(2000)
                    val intent = Intent(this@SplashActivity, PasswordActivity::class.java)
                    startActivity(intent)
                    this@SplashActivity.finish()
                    Log.d("TAG", "onCreate: started Password Activity")
                }

                false -> {
                    delay(2000)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    this@SplashActivity.finish()
                    Log.d("TAG", "onCreate: started Main Activity")
                }
            }
        }
    }

    private fun checkEnableDarkMode(){
        val enableDarkmode : Boolean = this.getSharedPreferences(
            "com.example.hn21_cpl_android_03g2_preferences",
            Context.MODE_PRIVATE
        ).getBoolean("enable_dark_mode", false)

        when (enableDarkmode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}