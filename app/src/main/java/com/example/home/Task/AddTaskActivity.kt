package com.example.home.Task

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.home.Adapter.UserDropDownAdapter
import com.example.home.Firebase.FirebaseClient
import com.example.home.Model.TaskDataModel
import com.example.home.Model.TaskModel
import com.example.home.Model.User
import com.example.home.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    var users = mutableListOf<User>()
    lateinit var selectedUserUid: String

    lateinit var newTasks: TaskDataModel

    lateinit var selectedCalender: Calendar
    var selectedPriority: String = "2"
    var selectedTag: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

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
        newTasks = requiredTaskHandler()

        newTasks.dueDate = selectedCalender.timeInMillis
        newTasks.creationDate = Calendar.getInstance().timeInMillis
        newTasks.priority = selectedPriority
        newTasks.tag = selectedTag

        if (selectedUserUid != Firebase.auth.currentUser?.uid!!){
            FirebaseClient().addTask(newTasks, selectedUserUid)
                .observe(this, {
                    if (it == true) {
                        Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        FirebaseClient().addTask(newTasks, Firebase.auth.uid.toString())
            .observe(this, {
                if (it == true) {
                    Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }

    private fun showDatePickerDialog(){
        //input view and Clickable image view
        val taskDueDateBtn = findViewById<ImageView>(R.id.imageViewAddDueDate)
        taskDueDateBtn.setOnClickListener {
            //get current date
            val currentDate = Calendar.getInstance()
            val currentYear = currentDate.get(Calendar.YEAR)
            val currentMonth = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            val dayDialog = DatePickerDialog(
                this, { view, year, month, dayOfMonth ->
                    val taskDueDate = findViewById<TextView>(R.id.textViewAddDueDate)
                    val dateString = "$dayOfMonth/${month + 1}/$year"
                    taskDueDate.text = dateString
                    val calender = Calendar.getInstance()
                    calender.set(year,month,dayOfMonth)
                    selectedCalender = calender
                },
                currentYear,
                currentMonth,
                day)
            dayDialog.show()
        }
    }

    private fun requiredTaskHandler(): TaskDataModel {
        val taskTitle = findViewById<TextInputLayout>(R.id.textInputAddTaskTitle)
        val taskDescription = findViewById<TextInputLayout>(R.id.textInputAddTaskDetails)

        return TaskDataModel(
            taskTitle.editText?.text.toString(),
            taskDescription.editText?.text.toString(),
            selectedUserUid,
        )
    }

    private fun priorityTaskHandler(){
        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            selectedPriority = chip.tag.toString()
        }
    }

    private fun tagsTaskHandler(){
        val chipGroupTag = findViewById<ChipGroup>(R.id.chipGroupTag)
        chipGroupTag.setOnCheckedChangeListener { group, checkedId ->
            val checkedPreviousTag = chipGroupTag.findViewWithTag<Chip>(selectedTag)
            val chip = findViewById<Chip>(checkedId)
            if (checkedPreviousTag != null && chip != null && checkedPreviousTag.id != chip.id) {
                checkedPreviousTag.setChipBackgroundColorResource(TaskModel().getTagColor(selectedTag))
                chip.setChipBackgroundColorResource(TaskModel().getCheckedColor(chip.tag.toString()))
                selectedTag = chip.tag.toString()
            }
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
            selectedUserUid = users[position].uid
            Picasso.get().load(Uri.parse(users[position].imageDataUri)).fit().into(userImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}