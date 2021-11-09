package com.example.home.Model

import java.io.Serializable

class User(var uid: String, var email: String): Serializable {
    var fullname: String? = ""
    var mobile: Int? = 0

    override fun toString(): String {
        return "$fullname"
    }
}