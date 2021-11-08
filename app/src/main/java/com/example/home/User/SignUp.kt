package com.example.home.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.home.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    //TODO:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var email = findViewById<EditText>(R.id.editInputSignupEmail)
        var password = findViewById<EditText>(R.id.editInputSignupPassword)
        var name = findViewById<EditText>(R.id.editInputSignupName)
        var mobile = findViewById<EditText>(R.id.editInputSignupMobile)
        var signUpBtn = findViewById<Button>(R.id.buttonSignup)


        signUpBtn.setOnClickListener {
            var auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener { result ->
                    if (result.isSuccessful){
                        val currentUser = auth.currentUser?.uid
                        val user = hashMapOf(
                            "email" to auth.currentUser?.email,
                            "name" to name.text.toString(),
                            "mobile" to mobile.text.toString()
                        )

                        var db = Firebase.firestore

                        db.collection(getString(R.string.collection_users))
                            .document(currentUser!!).set(user).addOnCompleteListener {
                                if (it.isSuccessful){
                                    //redirect to main view
                                } else {
                                    //handle error
                                }
                            }
                    } else {
                        //handle error
                    }
                }
                .addOnFailureListener {
                    //handle error
                }
        }

    }
}