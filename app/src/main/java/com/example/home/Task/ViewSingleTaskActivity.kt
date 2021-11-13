package com.example.home.Task

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.home.Firebase.FirebaseClient
import com.example.home.Model.TaskDataModel
import com.example.home.Model.TaskModel
import com.example.home.R
import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ViewSingleTaskActivity : AppCompatActivity() {

    var isPastDue = false

    lateinit var dataModel: TaskDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_single_task)

        setupToolbar()

        dataModel = intent.getSerializableExtra("task") as TaskDataModel

        getUserImage(dataModel)
        setData(dataModel)

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
            confirmDeletionRequest()
            true
        }
        menu?.findItem(R.id.view_task_edit)?.setOnMenuItemClickListener {
            val intent= Intent(this, EditTaskActivity::class.java)
            intent.putExtra("task", dataModel)
            intent.putExtra("isPastDue", isPastDue)
            startActivity(intent)
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun confirmDeletionRequest(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete ${dataModel.title}")
        alertDialog.setMessage(getString(R.string.deletion_confirmation_message))

        alertDialog.setPositiveButton("Yes"){ dialog, btnID ->
            FirebaseClient().deleteTask(dataModel, Firebase.auth.uid.toString())
            finish()
        }

        alertDialog.setNeutralButton("No"){ dialog, btnID ->
            dialog.cancel()
        }
        alertDialog.show()
    }

    private fun setData(data: TaskDataModel?) {
        if (data != null) {
            //Due Date: textViewViewDueDate
            findViewById<TextView>(R.id.textViewViewDueDate).text = TaskModel().convertToDate(data.dueDate)
            //Priority
            val priority = TaskModel().getPriority(data.priority!!) + " " + getString(R.string.priority)
            findViewById<TextView>(R.id.textViewViewTaskPriority).text = priority
            //chip tag
            findViewById<Chip>(R.id.chipTaskTag).setChipBackgroundColorResource(TaskModel().getTagColor(data.tag!!))
            //title
            findViewById<TextView>(R.id.textViewViewTaskTitle).text = data.title
            //Desc
            findViewById<TextView>(R.id.textViewViewTaskDetails).text = data.description
            //Completion Btn
            val buttonMarkComplete = findViewById<Button>(R.id.buttonMarkComplete)
            buttonMarkComplete.text = if (data.isDone == true) getString(R.string.mark_incomplete)
                                      else getString(R.string.mark_complete)
            buttonMarkComplete.setOnClickListener {
                data.isDone = data.isDone != true
                buttonMarkComplete.text = if (data.isDone!!) "Mark incomplete" else "Mark Complete"
                FirebaseClient().updateTaskStatus(data,Firebase.auth.uid!!)
            }
        }

    }

    private fun getUserImage(data: TaskDataModel?){
        FirebaseClient().getUser(data?.assignedMemberID!!).observe(this,{
            findViewById<TextView>(R.id.textViewViewMemberName).text = it.fullname
            FirebaseClient().getImageReference(it.imageUri).observe(this,{
                val imageView = findViewById<ImageView>(R.id.imageViewViewMemberTask)
                Picasso.get().load(Uri.parse(it.toString())).fit().into(imageView)
            })
        })
    }
}