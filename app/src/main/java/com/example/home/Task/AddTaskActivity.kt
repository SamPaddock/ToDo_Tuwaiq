package com.example.home.Task

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.example.home.Adapter.UserDropDownAdapter
import com.example.home.Firebase.FirebaseClient
import com.example.home.Model.Task
import com.example.home.Model.User
import com.example.home.R
import com.google.android.gms.maps.MapView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    var users = mutableListOf<User>()
    var selectedUserEmail = ""

    var newTasks: Task? = null

    var selectedCalender: Calendar? = null
    var selectedPriority: String = "1"
    var selectedTag: String = "2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        selectedUserEmail = Firebase.auth.currentUser?.email!!

        FirebaseClient().getUsers().observe(this,{
            users = it
            setUsersInDropDownList()
        })

        setupToolbar()

        showDatePickerDialog()
        priorityTaskHandler()
        tagsTaskHandler()

        val addTaskBtn = findViewById<Button>(R.id.buttonAddTask)

        addTaskBtn.setOnClickListener {
            addTaskToFirebase()
        }
    }

    private fun addTaskToFirebase() {
        newTasks = requiredTaskHandler(newTasks)

        if (selectedCalender != null) {
            newTasks?.dueDate = selectedCalender?.timeInMillis
        }
        newTasks?.creationDate = Calendar.getInstance().timeInMillis
        newTasks?.priority = selectedPriority
        newTasks?.tag = selectedTag

        if (newTasks != null) {
            FirebaseClient().addTask(newTasks!!, Firebase.auth.uid.toString())
                .observe(this, {
                    if (it == true) {
                        Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun showDatePickerDialog(){
        //input view and Clickable image view
        val taskDueDateBtn = findViewById<ImageView>(R.id.imageViewAddDueDate)
        taskDueDateBtn.setOnClickListener {
            //get current date
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            val dayDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val taskDueDate = findViewById<TextView>(R.id.textViewAddDueDate)
                    taskDueDate.text = "$dayOfMonth/${month+1}/$year"
                    var calender = Calendar.getInstance()
                    calender.set(year,month,dayOfMonth)
                    selectedCalender = calender
                },
                year,
                month,
                day)
            dayDialog.show()
        }
    }

    fun requiredTaskHandler(newTasks: Task?): Task {
        val taskTitle = findViewById<TextInputLayout>(R.id.textInputAddTaskTitle)
        val taskDescription = findViewById<TextInputLayout>(R.id.textInputAddTaskDetails)

        return Task(
            taskTitle.editText?.text.toString(),
            taskDescription.editText?.text.toString(),
            selectedUserEmail,
        )
    }

    fun priorityTaskHandler(){
        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            selectedPriority = chip.tag.toString()
        }
    }

    fun tagsTaskHandler(){
        val chipGroupTag = findViewById<ChipGroup>(R.id.chipGroupTag)
        chipGroupTag.setOnCheckedChangeListener { group, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            selectedTag = chip.tag.toString()
        }
    }

    private fun setupToolbar() {
        val mainToolbar = findViewById<Toolbar>(R.id.addTaskToolbar)
        mainToolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(mainToolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setUsersInDropDownList() {
        val adapter = UserDropDownAdapter(this, users)
        val membersDropDownList = findViewById<AutoCompleteTextView>(R.id.dropDownUser)
        membersDropDownList.setAdapter(adapter)
        membersDropDownList.setOnItemClickListener { parent, view, position, id ->
            //Image view
            val userImage = findViewById<ImageView>(R.id.imageViewAddMemberTask)
            selectedUserEmail = users[position].email
            Picasso.get().load(Uri.parse(users[position].imageDataUri)).fit().into(userImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}