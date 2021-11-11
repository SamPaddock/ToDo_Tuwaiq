package com.example.home.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Firebase.FirebaseClient
import com.example.home.MainActivity
import com.example.home.Model.Task
import com.example.home.R
import com.example.home.Task.ViewSingleTaskActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import java.lang.Math.abs
import java.util.*

class TaskAdapter(var context: Context, var data: MutableList<Task>): RecyclerView.Adapter<TaskHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var rowLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.folding_cell_task_view, parent, false)
        return TaskHolder(rowLayout)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.foldingCell.initialize(1000, Color.parseColor("#5D9091"),0)


        onBindViewUnfoldedCell(holder, position)
        onBindViewFoldedCell(holder, position)

        holder.foldingCell.setOnClickListener {
            holder.foldingCell.toggle(false)
        }

        holder.showTaskDetails.setOnClickListener {
            val baseView = context as MainActivity
            val intent = Intent(baseView,ViewSingleTaskActivity::class.java)
            intent.putExtra("task",data[position])
            baseView.startActivity(intent)
        }
    }

    private fun onBindViewFoldedCell(holder: TaskHolder, position: Int) {
        holder.taskImageFolded.setImageResource(R.mipmap.ic_app_icon)
        holder.taskDescriptionFolded.text = data[position].description
        holder.cardViewTagFolded.setBackgroundResource(getTagColor(data[position].tag!!))
        holder.taskTitleFolded.text = data[position].title
        holder.taskDueDateFolded.text = convertToDate(data[position].dueDate)
        holder.taskStatusFolded.setOnClickListener {
            val checked = holder.taskStatusFolded.isChecked
            if (checked){
                data[position].isDone = true
            }else {
                data[position].isDone = false
            }
            FirebaseClient().updateTaskStatus(data[position],Firebase.auth.uid!!)
        }
    }

    private fun onBindViewUnfoldedCell(holder: TaskHolder, position: Int) {
        holder.cardHolder.setBackgroundResource(getTagColor(data[position].tag!!))
        holder.taskTitle.text = data[position].title
        holder.taskpriority.text = getPriority(data[position].priority!!)
        holder.taskDaysRemining.text = remainingDays(data[position].dueDate)
        holder.taskStartEnd.text = context.getString(R.string.due_on_hint)+convertToDate(data[position].dueDate)
    }

    private fun getPriority(priority: String): String{
        return when (priority){
            "1" -> "High"
            "2" -> "Medium"
            "3" -> "Low"
            else -> "Medium"
        }
    }

    private fun getTagColor(tag: String): Int {
        return when (tag){
            "1" -> R.color.labelGreenUnChecked
            "2" -> R.color.labelYellowUnChecked
            "3" -> R.color.labelRedUnChecked
            "4" -> R.color.labelPurpleUnChecked
            "5" -> R.color.labelBlueUnChecked
            else -> R.color.labelGreenUnChecked
        }
    }

    private fun remainingDays(dueDate: Long?): String{
        val todayDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        if (dueDate != null) {
            endDate.timeInMillis = dueDate
        }
        val diff = abs(endDate.timeInMillis - todayDate.timeInMillis )
        val dayCount = diff/ (24 * 60 * 60 * 1000)
        return if (dayCount.toInt() == 1 || dayCount.toInt() == 0){
            "1 day"
        } else {
            "${dayCount} days"
        }
    }

    private fun convertToDate(dueDate: Long?): String{
        val endDate = Calendar.getInstance()
        if (dueDate != null) endDate.timeInMillis = dueDate
        val (endYear, endMonth, endDay) = Triple(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH),
            endDate.get(Calendar.DAY_OF_MONTH))
        return "$endDay/${endMonth+1}/$endYear "
    }
}

class TaskHolder(v: View): RecyclerView.ViewHolder(v) {
    var foldingCell = v.findViewById<FoldingCell>(R.id.folding_cell)
    var cardHolder = v.findViewById<CardView>(R.id.unfoldedCardView)

    var showTaskDetails = v.findViewById<Button>(R.id.buttonViewTask)
    //folding_cell_task_view unfolded components
    var taskTitle = v.findViewById<TextView>(R.id.unfoldedTextViewTaskTitle)
    var taskpriority = v.findViewById<TextView>(R.id.unfoldedTextViewTaskPriority)
    var taskDaysRemining = v.findViewById<TextView>(R.id.unfoldedTextViewTaskDaysRemining)
    var taskStartEnd = v.findViewById<TextView>(R.id.textViewStartEndDate)

    //folding_cell_task_view unfolded components
    var taskDescriptionFolded = v.findViewById<TextView>(R.id.foldedTextViewViewTaskDetails)
    var taskStatusFolded = v.findViewById<CheckBox>(R.id.foldedCheckBoxIsDone)
    var cardViewTagFolded = v.findViewById<CardView>(R.id.foldedCardViewViewImage)
    var taskImageFolded = v.findViewById<ImageView>(R.id.foldedImageViewViewMemberTask)
    var taskTitleFolded = v.findViewById<TextView>(R.id.textViewFoldedTaskTitle)
    var taskDueDateFolded = v.findViewById<TextView>(R.id.textViewDueDateFolded)
}