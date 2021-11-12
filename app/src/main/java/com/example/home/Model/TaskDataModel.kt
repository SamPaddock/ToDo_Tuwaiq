package com.example.home.Model

import java.io.Serializable

data class TaskDataModel(
    var title: String,
    var description: String,
    var assignedMemberID: String
): Serializable {
    var taskID: String? = null
    var dueDate: Long? = null
    var creationDate: Long? = null
    var priority: String? = null
    var tag: String? = null
    var isDone: Boolean? = false

    override fun toString(): String {
        return """
            title: $title
            description: $description
            assignedMemberID: $assignedMemberID
            taskID: $taskID
            dueDate: $dueDate
            creationDate: $creationDate
            priority: $priority
            tag: $tag
        """.trimIndent()
    }

}