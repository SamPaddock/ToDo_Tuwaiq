package com.example.home.Model

import java.util.*
import com.example.home.R

class TaskModel() {

    fun getPriority(priority: String): String{
        return when (priority){
            "1" -> "High"
            "2" -> "Medium"
            "3" -> "Low"
            else -> "Medium"
        }
    }

    fun getTagColor(tag: String): Int {
        return when (tag){
            "1" -> R.color.labelGreenUnChecked
            "2" -> R.color.labelYellowUnChecked
            "3" -> R.color.labelRedUnChecked
            "4" -> R.color.labelPurpleUnChecked
            "5" -> R.color.labelBlueUnChecked
            else -> R.color.labelGreenUnChecked
        }
    }

    fun remainingDays(dueDate: Long?): String{
        val todayDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        if (dueDate != null) {
            endDate.timeInMillis = dueDate
        }
        val diff = Math.abs(endDate.timeInMillis - todayDate.timeInMillis)
        val dayCount = diff/ (24 * 60 * 60 * 1000)
        return if (dayCount.toInt() == 1 || dayCount.toInt() == 0){
            "1 day"
        } else {
            "${dayCount} days"
        }
    }

    fun convertToDate(dueDate: Long?): String{
        val endDate = Calendar.getInstance()
        if (dueDate != null) endDate.timeInMillis = dueDate
        val (endYear, endMonth, endDay) = Triple(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH),
            endDate.get(Calendar.DAY_OF_MONTH))
        return "$endDay/${endMonth+1}/$endYear "
    }
}