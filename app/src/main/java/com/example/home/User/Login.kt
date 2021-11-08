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

        var email = findViewById<TextInputLayout>(R.id.textInputLoginEmail)
        var password = findViewById<TextInputLayout>(R.id.textInputLoginPassword)
        var loginBtn = findViewById<Button>(R.id.buttonLogin)
        var signUpBtn = findViewById<Button>(R.id.buttonSignupRedirect)

        loginBtn.setOnClickListener {
            var auth = Firebase.auth
            var editTextEmail = email.editText?.text.toString().trim()
            var editTextPassword = password.editText?.text.toString().trim()
            auth.signInWithEmailAndPassword(editTextEmail,editTextPassword)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful){
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        loginToApp(result)
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    if (it is FirebaseAuthInvalidCredentialsException){
                    }
                    Toast.makeText(this, "Login failed without completion ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        signUpBtn.setOnClickListener {

        }
    }

    fun loginToApp(result: Task<AuthResult>) {
        var intent = Intent(this, MainActivity::class.java)
        var userID = result.result?.user?.uid!!
        var userEmail = result.result?.user?.email!!
        var currentUser = User(userID,userEmail)
        intent.putExtra("user", currentUser)
        startActivity(intent)
    }

}