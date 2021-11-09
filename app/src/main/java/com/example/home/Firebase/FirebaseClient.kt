package com.example.home.Firebase

import com.example.home.Model.Task
import com.example.home.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseClient {

    var db: FirebaseFirestore? = null

    fun createDB(){
        var db = Firebase.firestore
    }

    fun getUser(){

    }
}