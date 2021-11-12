package com.example.home.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Firebase.FirebaseClient
import com.example.home.MainActivity
import com.example.home.Model.TaskDataModel
import com.example.home.Model.TaskModel
import com.example.home.R
import com.example.home.Task.ViewSingleTaskActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import com.squareup.picasso.Picasso
import java.lang.Math.abs
import java.util.*

class TaskAdapter(var lifecycleOwner: LifecycleOwner, var context: Context, var data: MutableList<TaskDataModel>): RecyclerView.Adapter<TaskHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val rowLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.folding_cell_task_view, parent, false)
        return TaskHolder(rowLayout)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.foldingCell.initialize(500, Color.parseColor("#5D9091"),0)

        onBindViewImage(holder, position)
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

    private fun onBindViewImage(holder: TaskHolder, position: Int){
        FirebaseClient().getUser(data[position]?.assignedMemberID!!).observe(lifecycleOwner,{
            println("getting image")
            FirebaseClient().getImageReference(it.imageUri).observe(lifecycleOwner,{
                println("Got image")
                Picasso.get().load(Uri.parse(it.toString())).fit().into(holder.taskImageFolded)
            })
        })
    }

    private fun onBindViewFoldedCell(holder: TaskHolder, position: Int) {
        holder.taskImageFolded.setImageResource(R.mipmap.ic_app_icon)
        holder.taskDescriptionFolded.text = data[position].description
        holder.cardViewTagFolded.setBackgroundResource(TaskModel().getTagColor(data[position].tag!!))
        holder.taskTitleFolded.text = data[position].title
        holder.taskStatusFolded.setOnClickListener {
            val checked = holder.taskStatusFolded.isChecked
            data[position].isDone = checked
            FirebaseClient().updateTaskStatus(data[position],Firebase.auth.uid!!)
        }
    }

    private fun onBindViewUnfoldedCell(holder: TaskHolder, position: Int) {
        holder.cardHolder.setBackgroundResource(TaskModel().getTagColor(data[position].tag!!))
        holder.taskTitle.text = data[position].title
        holder.taskpriority.text = TaskModel().getPriority(data[position].priority!!)
        holder.taskDaysRemining.text = TaskModel().remainingDays(data[position].dueDate)
        val s = context.getString(R.string.due_on_hint)+ " " + TaskModel()
            .convertToDate(data[position].dueDate)
        holder.taskStartEnd.text = s
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
}