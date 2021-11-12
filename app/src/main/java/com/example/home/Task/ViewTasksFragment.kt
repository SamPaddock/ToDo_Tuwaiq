package com.example.home.Task

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Adapter.TaskAdapter
import com.example.home.Firebase.FirebaseClient
import com.example.home.MainActivity
import com.example.home.Model.TaskDataModel
import com.example.home.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ViewTasksFragment : Fragment() {

    var taskList = mutableListOf<TaskDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_view_tasks, container, false)
        taskList.removeAll(taskList)

        setHasOptionsMenu(true)

        FirebaseClient().getAllTasks(Firebase.auth.uid!!).observe(viewLifecycleOwner,{
            println("Got task: ${it}")
            taskList = it
            val recycledView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
            recycledView.layoutManager = LinearLayoutManager(activity)
            recycledView.adapter = context?.let { TaskAdapter(this.viewLifecycleOwner,it, taskList) }
        })

        val addTaskBtn = inflate.findViewById<FloatingActionButton>(R.id.fabAddTask)

        addTaskBtn.setOnClickListener {
            val baseView = context as MainActivity
            val intent = Intent(baseView,AddTaskActivity::class.java)
            baseView.startActivity(intent)
        }

        return inflate
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        super.onCreateOptionsMenu(menu,inflater)
    }
}