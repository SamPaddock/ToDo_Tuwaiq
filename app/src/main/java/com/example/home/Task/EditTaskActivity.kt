package com.example.home.Task

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import  android.os.Bundle
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

class EditTaskActivity : AppCompatActivity() {

    lateinit var selectedUserEmail: String

    lateinit var dataModel: TaskDataModel
    var inPassDue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        selectedUserEmail = Firebase.auth.currentUser?.email!!

        setupToolbar()

        dataModel = intent.getSerializableExtra("task") as TaskDataModel
        inPassDue = intent.getBooleanExtra("isPastDue", false)

        setTaskData(dataModel,inPassDue)

        //buttonSaveChanges
        val saveBtn = findViewById<Button>(R.id.buttonSaveChanges)
        saveBtn.setOnClickListener {
            saveTaskChanges()
        }

    }

    fun setTaskData(data: TaskDataModel, inPassDue: Boolean) {
        findViewById<TextInputLayout>(R.id.textInputEditTaskTitle)
            .editText?.text?.append(data.title)
        findViewById<TextInputLayout>(R.id.textInputEditTaskDetails)
            .editText?.text?.append(data.description)

        if (!inPassDue){
            val textViewCreationDate = findViewById<TextView>(R.id.textViewEditCreationDate)
            val imageViewCreationDate = findViewById<ImageView>(R.id.imageViewEditCreationDate)
            val creationDateCalendar = Calendar.getInstance()
            textViewCreationDate.text = TaskModel().convertToDate(data.creationDate!!)
            creationDateCalendar.timeInMillis = data.creationDate!!
            showDatePickerDialog(creationDateCalendar, imageViewCreationDate,
                textViewCreationDate, false)
        }

        val textViewDueDate = findViewById<TextView>(R.id.textViewEditDueDate)
        val imageViewDueDate = findViewById<ImageView>(R.id.imageViewEditDueDate)
        val dueDateCalendar = Calendar.getInstance()
        textViewDueDate.text = TaskModel().convertToDate(data.dueDate!!)
        dueDateCalendar.timeInMillis = data.dueDate!!
        showDatePickerDialog(dueDateCalendar, imageViewDueDate, textViewDueDate, true)

        priorityTaskHandler(data)

        tagsTaskHandler(data)
    }

    fun saveTaskChanges(){
        //title
        val editTextTitle = findViewById<TextInputLayout>(R.id.textInputEditTaskTitle)
        dataModel.title = editTextTitle.editText?.text.toString()
        //description
        val editTextDescription = findViewById<TextInputLayout>(R.id.textInputEditTaskDetails)
        dataModel.description = editTextDescription.editText?.text.toString()

        //member
        dataModel.assignedMemberID = selectedUserEmail

        Toast.makeText(this, "task ID: ${dataModel.taskID}", Toast.LENGTH_SHORT).show()
        FirebaseClient().updateTask(dataModel, Firebase.auth.uid.toString()).observe(this,{
            if (it){
                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun showDatePickerDialog(dateCalendar: Calendar, imageViewDate: ImageView,
        textViewDate: TextView, isDueDate: Boolean) {
        imageViewDate.setOnClickListener {
            //get current date
            val currentYear = dateCalendar.get(Calendar.YEAR)
            val currentMonth = dateCalendar.get(Calendar.MONTH)
            val day = dateCalendar.get(Calendar.DAY_OF_MONTH)
            val dayDialog = DatePickerDialog(
                this, { view, year, month, dayOfMonth ->
                    val dateString = "$dayOfMonth/${month + 1}/$year"
                    textViewDate.text = dateString
                    val calender = Calendar.getInstance()
                    calender.set(year,month,dayOfMonth)
                    if (isDueDate){
                        dataModel.dueDate = calender.timeInMillis
                    }else{
                        dataModel.creationDate = calender.timeInMillis
                    }
                },
                currentYear,
                currentMonth,
                day)
            dayDialog.show()
        }
    }

    private fun priorityTaskHandler(data: TaskDataModel) {
        val chipGroupPriority = findViewById<ChipGroup>(R.id.chipEditGroupPriority)
        val checkedPriority = chipGroupPriority.findViewWithTag<Chip>(data.priority)
        chipGroupPriority.check(checkedPriority.id)
        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            dataModel.priority = chip.tag.toString()
        }
    }

    private fun tagsTaskHandler(data: TaskDataModel) {
        val chipGroupTag = findViewById<ChipGroup>(R.id.chipEditGroupTag)
        val checkedTag = chipGroupTag.findViewWithTag<Chip>(data.tag)
        checkedTag.setChipBackgroundColorResource(TaskModel().getCheckedColor(data.tag!!))
        chipGroupTag.check(checkedTag.id)
        chipGroupTag.setOnCheckedChangeListener { group, checkedId ->
            val checkedPreviousTag = chipGroupTag.findViewWithTag<Chip>(dataModel.tag)
            val chip = findViewById<Chip>(checkedId)
            if (checkedPreviousTag != null && chip != null && checkedPreviousTag.id != chip.id) {
                checkedPreviousTag.setChipBackgroundColorResource(TaskModel().getTagColor(dataModel.tag!!))
                chip.setChipBackgroundColorResource(TaskModel().getCheckedColor(chip.tag.toString()))
                dataModel.tag = chip.tag.toString()
            }
        }
    }

    private fun setupToolbar() {
        val mainToolbar = findViewById<Toolbar>(R.id.editTaskToolbar)
        mainToolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(mainToolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        menu?.findItem(R.id.cancel_action)?.setOnMenuItemClickListener {
            finish()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }
}