package com.example.home.Firebase

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.home.Model.TaskDataModel
import com.example.home.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

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

    fun updateTaskStatus(taskDataModel: TaskDataModel?, userID: String): LiveData<Boolean>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<Boolean>()

        if (taskDataModel != null) {
            dbFirestore?.collection("Users")?.document(userID)
                ?.collection("Tasks")?.document(taskDataModel.taskID.toString())
                ?.update("isDone", taskDataModel.isDone)?.addOnCompleteListener {
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
                                println("User: Got image")
                                val user = User(
                                    document?.id!!,
                                    document.getString("Email")!!
                                )
                                user.fullname = document.getString("Name")!!
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

    fun addTask(taskDataModel: TaskDataModel?, userID: String): LiveData<Boolean>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<Boolean>()

        if (taskDataModel != null) {
            dbFirestore?.collection("Users")?.document(userID)
                ?.collection("Tasks")?.add(taskDataModel)
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

    fun getAllTasks(userID: String): LiveData<MutableList<TaskDataModel>>{
        if (dbFirestore == null) createDBFirestore()

        val liveDataTask = MutableLiveData<MutableList<TaskDataModel>>()
        dbFirestore?.collection("Users")?.document(userID)
            ?.collection("Tasks")?.get()
            ?.addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful && snapshot.result != null) {
                    val listOfTasks = mutableListOf<TaskDataModel>()
                    for (document in snapshot.result!!) {
                        println("Task: ${document.data}")
                        val task = TaskDataModel(
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

    fun getImageReference(imageUri: String): LiveData<String>{
        if (dbFBStorage == null) createDBStorage()

        val liveDataImage = MutableLiveData<String>()

        dbFBStorage?.getReferenceFromUrl(imageUri)?.downloadUrl
            ?.addOnCompleteListener {
                var imageRef = ""
                if (it.isSuccessful){ imageRef = it.result.toString();println("Storage: Got image")}
                Log.d(TAG,"Image loading statues: ${it.result.toString()}")
                liveDataImage.postValue(imageRef)
            }?.addOnFailureListener { Log.d(TAG,"Error: ${it.message}") }
        return liveDataImage
    }
}




















