package com.example.home.Task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.home.Model.Task
import com.example.home.R

class ViewSingleTaskActivity : AppCompatActivity() {

    var isPastDue = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_single_task)

        setupToolbar()

        var data = intent.getSerializableExtra("task") as Task

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

    fun setData(data: Task?) {
        if (data != null) {
            findViewById<TextView>(R.id.textViewViewTaskTitle).text = data.title
            findViewById<TextView>(R.id.textViewViewTaskDetails).text = data.description
            findViewById<TextView>(R.id.textViewViewDueDate).text = data.dueDate.toString()
            findViewById<ImageView>(R.id.imageViewViewMemberTask).setImageResource(R.mipmap.ic_app_icon)
        }

    }
}