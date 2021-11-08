package com.example.home.Task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Adapter.TaskAdapter
import com.example.home.MainActivity
import com.example.home.Model.Task
import com.example.home.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ramotion.foldingcell.FoldingCell
import java.util.*

class ViewTasksFragment : Fragment() {

    var taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_view_tasks, container, false)

        getFromFireBase()

        val recycledView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
        recycledView.layoutManager = LinearLayoutManager(activity)
        recycledView.adapter = TaskAdapter(taskList)

        val addTaskBtn = inflate.findViewById<FloatingActionButton>(R.id.fabAddTask)

        addTaskBtn.setOnClickListener {
            (context as MainActivity).displayFragment(AddTaskFragment())
        }

        return inflate
    }

    fun getFromFireBase(){
        var user = Firebase.auth.currentUser?.uid
        var db = Firebase.firestore

        db.collection(getString(R.string.user)).document(user.toString())
            .collection(getString(R.string.task)).get()
            .addOnCompleteListener { document ->
                if(document.isSuccessful && document.result != null){
                    var data = document.result!!
                    for (task in data){
                        var taskData = task.data
                        taskList.add(Task(
                            task.id,
                            taskData.get(getString(R.string.title)) as String,
                            taskData.get(getString(R.string.description)) as String,
                            taskData.get(getString(R.string.dueDate)) as Long,
                            taskData.get(getString(R.string.creationDate)) as Long
                        ))
                    }
                }
            }.addOnFailureListener { exception ->
                showSnackBar("There was an issue: ${exception.message}")
            }
    }

    fun showSnackBar(msg: String){
        Snackbar.make(this.requireView(),msg, Snackbar.LENGTH_SHORT).show()
    }

    fun populateTask(): MutableList<Task>{
        var list = mutableListOf<Task>()
        list.add(Task("1","task1","testing",Calendar.getInstance().timeInMillis,Calendar.getInstance().timeInMillis))
        list.add(Task("2","task2","testing second row",Calendar.getInstance().timeInMillis,Calendar.getInstance().timeInMillis))
        list.add(Task("3","task3","testing third row",Calendar.getInstance().timeInMillis,Calendar.getInstance().timeInMillis))
        return list
    }
}