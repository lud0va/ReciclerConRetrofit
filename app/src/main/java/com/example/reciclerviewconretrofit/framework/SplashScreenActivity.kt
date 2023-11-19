package com.example.reciclerviewconretrofit.framework

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

import com.example.reciclerviewconretrofit.framework.main.MainActivity
import com.example.recyclerviewenhanced.R


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler().postDelayed({
            // Esto se ejecutará después del tiempo de espera
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

