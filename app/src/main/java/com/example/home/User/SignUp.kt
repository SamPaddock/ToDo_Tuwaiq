package com.example.home.User

import android.content.Intent
import android.net.Uri
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

class SignUp : AppCompatActivity() {

    lateinit var imageUri: String
    lateinit var imgData: Uri

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
            imgData = data?.data!!
            findViewById<ImageView>(R.id.imageViewSelectedPhoto).setImageURI(imgData)
        }
    }

    private fun signUpUser(email: EditText, password: EditText, name: EditText) {
        val editInputEmail = email.text.toString()
        val editInputPassword = password.text.toString()
        val editInputName = name.text.toString()

        if (emailVerification(editInputEmail) && imgData.path?.isEmpty() != true) {
            val user = User("", editInputEmail)
            user.fullname = editInputName

            FirebaseClient().signupUser(
                editInputEmail,
                editInputPassword
            ).observe(this, {
                if (it == true) {
                    addToUserProfile(user)
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

    private fun addToUserProfile(user: User) {
        FirebaseClient().setImage(imgData).observe(this,{ imageUri ->
            val newUser = hashMapOf(
                "Email" to user.email,
                "Name" to user.fullname,
                "Profile" to imageUri.toString()
            )
            FirebaseClient().createUserAccount(newUser)
            loginToApp()
        })
    }

    private fun loginToApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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