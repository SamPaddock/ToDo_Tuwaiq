package com.example.home.Task

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.home.Firebase.FirebaseClient
import com.example.home.Model.TaskDataModel
import com.example.home.Model.TaskModel
import com.example.home.R
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso

class ViewSingleTaskActivity : AppCompatActivity() {

    var isPastDue = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_single_task)

        setupToolbar()

        val data = intent.getSerializableExtra("task") as TaskDataModel

        getUserImage(data)
        setData(data)

    }

    private fun setupToolbar() {
        val mainToolbar = findViewById<Toolbar>(R.id.viewSingleTaskToolbar)
        mainToolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(mainToolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_task_menu,menu)
        menu?.findItem(R.id.view_task_delete)?.setOnMenuItemClickListener {
            //TODO: delete from db
            true
        }
        menu?.findItem(R.id.view_task_edit)?.setOnMenuItemClickListener {
            var intent= Intent(this, EditTaskActivity::class.java)
            intent.putExtra("task", intent.getSerializableExtra("task"))
            intent.putExtra("isPastDue", isPastDue)
            startActivity(intent)
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun setData(data: TaskDataModel?) {
        if (data != null) {
            //Due Date: textViewViewDueDate
            findViewById<TextView>(R.id.textViewViewDueDate).text = TaskModel().convertToDate(data.dueDate)
            //Priority: textViewViewTaskPriority
            val priority = TaskModel().getPriority(data.priority!!) + " " + getString(R.string.priority)
            findViewById<TextView>(R.id.textViewViewTaskPriority).text = priority
            //chip tag: chipTaskTag
            findViewById<Chip>(R.id.chipTaskTag).setChipBackgroundColorResource(TaskModel().getTagColor(data.tag!!))
            //title: textViewViewTaskTitle
            findViewById<TextView>(R.id.textViewViewTaskTitle).text = data.title
            //Desc: textViewViewTaskDetails
            findViewById<TextView>(R.id.textViewViewTaskDetails).text = data.description
        }

    }

    fun getUserImage(data: TaskDataModel?){
        FirebaseClient().getUser(data?.assignedMemberID!!).observe(this,{
            findViewById<TextView>(R.id.textViewViewMemberName).text = it.fullname
            FirebaseClient().getImageReference(it.imageUri).observe(this,{
                val imageView = findViewById<ImageView>(R.id.imageViewViewMemberTask)
                Picasso.get().load(Uri.parse(it.toString())).fit().into(imageView)
            })
        })
    }
}