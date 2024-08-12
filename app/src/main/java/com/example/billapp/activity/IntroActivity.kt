package com.example.billapp.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.billapp.R


class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("IntroActivity", "onCreate called")
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val tvAppNameIntro: TextView = findViewById(R.id.tv_app_name_intro)
        val typeface: Typeface = Typeface.createFromAsset(assets, "Montserrat-Bold.ttf")
        tvAppNameIntro.typeface = typeface

        val btnSignUpIntro: Button = findViewById(R.id.btn_sign_up_intro)
        btnSignUpIntro.setOnClickListener {
            // Launch the sign up screen.
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }

        val btnSignInIntro: Button = findViewById(R.id.btn_sign_in_intro)
        btnSignInIntro.setOnClickListener {
            // Launch the sign up screen.
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }


    }
}