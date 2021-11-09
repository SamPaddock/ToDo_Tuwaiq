package com.example.home.Task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.home.Adapter.UserDropDownAdapter
import com.example.home.Model.User
import com.example.home.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddTaskActivity : AppCompatActivity() {

    var users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        setupToolbar()
        val db = Firebase.firestore
        getUsers(db)
        setUsersInDropDownList()

        //input view
        var taskTitle = findViewById<TextInputLayout>(R.id.textInputAddTaskTitle)
        var taskDescription = findViewById<TextInputLayout>(R.id.textInputAddTaskDetails)
        var taskDueDate = findViewById<TextView>(R.id.textViewAddDueDate)
        var taskMember = findViewById<TextInputLayout>(R.id.textInputAddMember)
        //Image view
        var userImage = findViewById<ImageView>(R.id.imageViewAddMemberTask)
        //Clickable image view
        var taskDueDateBtn = findViewById<ImageView>(R.id.imageViewAddDueDate)
        var addLocationBtn = findViewById<ImageView>(R.id.imageViewAddTaskLocation)
        //multiple chip views
        //Priority
        var chipGroupPriority = findViewById<ChipGroup>(R.id.chipGroupPriority)
        var chipLow = findViewById<Chip>(R.id.chip_low)
        var chipMedium = findViewById<Chip>(R.id.chip_medium)
        var chipHigh = findViewById<Chip>(R.id.chip_high)
        //Tags
        var chipGroupTag = findViewById<ChipGroup>(R.id.chipGroupTag)
        var chipGreenTag = findViewById<Chip>(R.id.chipLabelGreen)
        var chipYellowTag = findViewById<Chip>(R.id.chipLabelYellow)
        var chipRedTag = findViewById<Chip>(R.id.chipLabelRed)
        var chipPurpleTag = findViewById<Chip>(R.id.chipLabelPurple)
        var chipBlueTag = findViewById<Chip>(R.id.chipLabelBlue)



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
        findViewById<AutoCompleteTextView>(R.id.dropDownUser).setAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun getUsers(db: FirebaseFirestore) {
        db.collection(getString(R.string.user)).get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful && snapshot.result != null){
                    for (user in snapshot.result!!){
                        val email = user.get("Email") as String
                        val name = user.get("Name") as String
                        val dbUser = User(user.id, email)
                        dbUser.fullname = name
                        users.add(dbUser)
                    }
                }

            }
    }
}