package com.example.home.Model

import java.io.Serializable

class Task(
    var taskID: String,
    var title: String,
    var desciption: String,
    var dueDate: Long,
    var creationDate: Long): Serializable {
}