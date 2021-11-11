package com.example.home.Firebase

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.home.Model.Task
import com.example.home.Model.User
import com.example.home.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class FirebaseClient() {

    var dbFirestore: FirebaseFirestore? = null
    var dbFBStorage: FirebaseStorage? = null
    var dbFBAuth: FirebaseAuth? = null

    fun createDBFirestore(){
        dbFirestore = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true).build()
        dbFirestore!!.firestoreSettings = settings
    }
    fun createDBStorage(){dbFBStorage = Firebase.storage}
    fun createDBAuth(){dbFBAuth = Firebase.auth}

    fun signInUser(){

    }

    fun updateTaskStatus(task: Task?,userID: String): LiveData<Boolean>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<Boolean>()

        if (task != null) {
            dbFirestore?.collection("Users")?.document(userID)
                ?.collection("Tasks")?.document(task.taskID.toString())
                ?.update("isDone", task.isDone)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        liveDataTask.postValue(true)
                    } else {
                        Log.d(TAG, "Error: ${it.result.toString()}")
                    }
                }?.addOnFailureListener {
                    liveDataTask.postValue(false)
                    Log.d(TAG, "Error: ${it.message}")
                }
        }

        return liveDataTask
    }

    fun getUser(assignedMemberID: String): LiveData<User>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataUser = MutableLiveData<User>()

        dbFirestore?.collection("Users")?.get()
            ?.addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful && snapshot.result != null) {
                    val result = snapshot.result
                    if (result != null) {
                        for (document in result.documents){
                            if (document.getString("Email") == assignedMemberID){
                                val user = User(
                                    document?.id!!,
                                    document.getString("Email")!!
                                )
                                user.imageUri = document.getString("Profile")!!
                                liveDataUser.postValue(user)
                            }
                        }
                    }
                }
            }
        return liveDataUser
    }

    fun getUsers(): LiveData<MutableList<User>> {
        if (dbFirestore == null) createDBFirestore()

        val liveDataUser = MutableLiveData<MutableList<User>>()

        dbFirestore?.collection("Users")?.get()
            ?.addOnCompleteListener { snapshot ->
                Log.d(TAG, "Init -> User Data: ${snapshot.getResult()}")
                if (snapshot.isSuccessful && snapshot.result != null) {
                    val listOfUsers = mutableListOf<User>()
                    for (user in snapshot.result!!) {
                        val email = user.get("Email") as String
                        val name = user.get("Name") as String
                        val image = user.get("Profile") as String
                        val dbUser = User(user.id, email)
                        dbUser.fullname = name
                        dbUser.imageUri = image
                        listOfUsers.add(dbUser)
                    }
                    liveDataUser.postValue(listOfUsers)
                }

            }
        return liveDataUser
    }

    fun addTask(task: Task?,userID: String): LiveData<Boolean>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<Boolean>()

        if (task != null) {
            dbFirestore?.collection("Users")?.document(userID)
                ?.collection("Tasks")?.add(task)
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                        liveDataTask.postValue(true)
                    } else {
                        Log.d(TAG, "Error: ${it.result.toString()}")
                    }
                }?.addOnFailureListener {
                    liveDataTask.postValue(false)
                    Log.d(TAG, "Error: ${it.message}")
                }
        }
        return liveDataTask
    }

    fun getAllTasks(userID: String): LiveData<MutableList<Task>>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<MutableList<Task>>()
        dbFirestore?.collection("Users")?.document(userID)
            ?.collection("Tasks")?.get()
            ?.addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful && snapshot.result != null) {
                    val listOfTasks = mutableListOf<Task>()
                    for (document in snapshot.result!!) {
                        println("Task: ${document.data}")
                        val task = Task(
                            document.getString("title") as String,
                            document.getString("description") as String,
                            document.getString("assignedMemberID") as String
                        )
                        task.priority = document.getString("priority") ?: "2"
                        task.tag = document.getString("tag") ?: "1"
                        task.creationDate = document.getLong("creationDate")
                        task.dueDate = document.getLong("dueDate")
                        task.taskID = document.id
                        listOfTasks.add(task)
                    }
                    liveDataTask.postValue(listOfTasks)
                }
            }

        return liveDataTask
    }

    fun getImageReference(imageUri: String): String{
        var imageRef = ""
        dbFBStorage?.getReferenceFromUrl(imageUri)?.downloadUrl
            ?.addOnCompleteListener {
                if (it.isSuccessful){ imageRef = it.result.toString() }
            }?.addOnFailureListener { Log.d(TAG,"Error: ${it.message}") }
        return imageRef
    }
}




















