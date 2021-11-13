package com.example.home.User

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.home.Firebase.FirebaseClient
import com.example.home.MainActivity
import com.example.home.Model.User
import com.example.home.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    lateinit var imageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val email = findViewById<EditText>(R.id.editInputSignupEmail)
        val password = findViewById<EditText>(R.id.editInputSignupPassword)
        val name = findViewById<EditText>(R.id.editInputSignupName)
        val uploadImageBtn = findViewById<Button>(R.id.buttonSelectPhoto)
        val signUpBtn = findViewById<Button>(R.id.buttonSignup)

        uploadImageBtn.setOnClickListener {
            val takePictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(takePictureIntent, 2)
        }

        signUpBtn.setOnClickListener { signUpUser(email, password, name) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            val imgData = data?.data
            findViewById<ImageView>(R.id.imageViewSelectedPhoto).setImageURI(imgData)
            if (imgData != null) {
                FirebaseClient().setImage(imgData).observe(this,{
                    imageUri = it.toString()
                })
            }
        }
    }

    private fun signUpUser(email: EditText, password: EditText, name: EditText) {
        val editInputEmail = email.text.toString()
        val editInputPassword = password.text.toString()
        val editInputName = name.text.toString()

        if (emailVerification(editInputEmail) && imageUri.isNotEmpty()) {
            val user = User("", editInputEmail)
            user.fullname = editInputName
            user.imageUri = imageUri

            FirebaseClient().signupUser(
                editInputEmail,
                editInputPassword,
                user
            ).observe(this, {
                if (it == true) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.network_warning), Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            Toast.makeText(this, getString(R.string.email_photo_warning),Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun emailVerification(email: String): Boolean {
        //verify email is not empty and the email is in a correct format
        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email is valid", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}