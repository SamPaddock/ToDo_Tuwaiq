package com.example.home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Model.Task
import com.example.home.R
import com.ramotion.foldingcell.FoldingCell
import java.util.*

class TaskAdapter(var data: MutableList<Task>): RecyclerView.Adapter<TaskHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var rowLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.folding_cell_task_view, parent, false)
        return TaskHolder(rowLayout)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        var time =  data[position].dueDate
        var calender = Calendar.getInstance()
        calender.timeInMillis = time
        var date = "${calender.get(Calendar.DATE)}"
        holder.taskTitle.text = data[position].title
        holder.taskDescription.text = data[position].desciption
        holder.taskDueDate.text = date
        holder.taskImage.setImageResource(R.mipmap.ic_app_icon)
        holder.taskTitleFolded.text = data[position].title
        holder.taskDescriptionFolded.text = data[position].desciption
        holder.taskDueDateFolded.text = date
        holder.taskImageFolded.setImageResource(R.mipmap.ic_app_icon)

        holder.foldingCell.setOnClickListener {
            holder.foldingCell.toggle(false)
        }
    }

    override fun getItemCount() = data.size

}

class TaskHolder(v: View): RecyclerView.ViewHolder(v) {
    var foldingCell = v.findViewById<FoldingCell>(R.id.folding_cell)
    //folding_cell_task_view unfolded components
    var taskTitle = v.findViewById<TextView>(R.id.unfoldedTextViewTaskTitle)
    var taskDescription = v.findViewById<TextView>(R.id.unfoldedTextViewTaskDetails)
    var taskDueDate = v.findViewById<TextView>(R.id.unfoldedTextViewTaskDueDate)
    var taskImage = v.findViewById<ImageView>(R.id.unfoldedImageViewMemberTask)

    //folding_cell_task_view unfolded components
    var taskTitleFolded = v.findViewById<TextView>(R.id.foldedTextViewViewTaskTitle)
    var taskDescriptionFolded = v.findViewById<TextView>(R.id.foldedTextViewViewTaskDetails)
    var taskDueDateFolded = v.findViewById<TextView>(R.id.foldedTextViewViewDueDate)
    var taskImageFolded = v.findViewById<ImageView>(R.id.foldedImageViewViewMemberTask)
}