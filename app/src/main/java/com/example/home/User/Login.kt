package com.example.home.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.home.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var email = findViewById<EditText>(R.id.editInputSignupEmail)
        var password = findViewById<EditText>(R.id.editInputLoginPassword)
        var loginBtn = findViewById<Button>(R.id.buttonLogin)
        var signUpBtn = findViewById<Button>(R.id.buttonSignupRedirect)

        loginBtn.setOnClickListener {
            var auth = Firebase.auth
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener { result ->
                    if (result.isSuccessful){

                    } else {

                    }
                }
                .addOnFailureListener {

                }
        }

        signUpBtn.setOnClickListener {

        }
    }
}