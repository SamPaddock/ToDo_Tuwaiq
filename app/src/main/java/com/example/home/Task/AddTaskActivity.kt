package com.example.home.Task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.home.Model.User
import com.example.home.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddTaskActivity : AppCompatActivity() {

    var users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val db = Firebase.firestore

        getUsers(db)
    }

    fun getUsers(db: FirebaseFirestore) {
        db.collection(getString(R.string.user)).get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful && snapshot.result != null){
                    for (user in snapshot.result!!){
                        var email = user.get(getString(R.string.email)) as String
                        var user = User(user.id,user.get(getString(R.string.name)) as String)
                        user.email = email
                        users.add(user)
                    }
                }

            }
    }
}