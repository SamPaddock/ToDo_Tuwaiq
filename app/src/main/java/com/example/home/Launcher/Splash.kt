package com.example.home.Launcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.home.MainActivity
import com.example.home.R
import com.example.home.User.Login
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent()
            if (Firebase.auth.currentUser != null){
                intent.setClass(this, MainActivity::class.java)
            } else {
                intent.setClass(this, Login::class.java)
            }
            startActivity(intent)
            finish()
        }, 5000)
    }
}