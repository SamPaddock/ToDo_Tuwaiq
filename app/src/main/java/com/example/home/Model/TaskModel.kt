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

    fun getCheckedColor(tag: String): Int {
        return when (tag){
            "1" -> R.color.labelGreenChecked
            "2" -> R.color.labelYellowChecked
            "3" -> R.color.labelRedChecked
            "4" -> R.color.labelPurpleChecked
            "5" -> R.color.labelBlueChecked
            else -> R.color.labelGreenChecked
        }
    }

    fun remainingDays(dueDate: Long?): Int{
        val todayDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        if (dueDate != null) {
            endDate.timeInMillis = dueDate
        }
        val diff = Math.abs(todayDate.timeInMillis - endDate.timeInMillis)
        val dayCount = (diff/ (24 * 60 * 60 * 1000))
        return dayCount.toInt()
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

    fun isPastDue(dueDate: Long? = 0): Boolean {
        val currentDate = Calendar.getInstance().timeInMillis
        return dueDate!! <= currentDate
    }
}