package com.example.home.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.home.MainActivity
import com.example.home.Model.User
import com.example.home.R
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<TextInputLayout>(R.id.textInputLoginEmail)
        val password = findViewById<TextInputLayout>(R.id.textInputLoginPassword)
        val loginBtn = findViewById<Button>(R.id.buttonLogin)
        val signUpBtn = findViewById<Button>(R.id.buttonSignupRedirect)

        loginBtn.setOnClickListener {
            loginToApp(email, password)
        }

        signUpBtn.setOnClickListener {

        }
    }

    private fun loginToApp(email: TextInputLayout, password: TextInputLayout) {
        val auth = Firebase.auth
        val editTextEmail = email.editText?.text.toString().trim()
        val editTextPassword = password.editText?.text.toString().trim()
        auth.signInWithEmailAndPassword(editTextEmail, editTextPassword)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    loginToApp(result)
                }
            }
            .addOnFailureListener {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(
                        this,
                        "Login failed without completion ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Unknown issue - ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

    private fun loginToApp(result: Task<AuthResult>) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}