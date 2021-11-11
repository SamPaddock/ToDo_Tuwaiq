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
import com.example.home.Model.Task
import com.example.home.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ViewTasksFragment : Fragment() {

    var taskList = mutableListOf<Task>()

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
            recycledView.adapter = context?.let { TaskAdapter(it, taskList) }
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


    //TODO: Remove after confirming firebase DB is functional
    fun populateTask(){
        taskList.add(Task("task1","task1","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer porta nisi risus, in ultrices odio suscipit quis. Vestibulum lorem augue, maximus eget volutpat sed, volutpat nec dolor. Praesent vitae rhoncus libero. Fusce ut nibh mi. Ut venenatis sollicitudin leo quis gravida. Quisque tempus porttitor nibh vel sodales. Morbi nibh magna, sagittis in dui non, suscipit pulvinar turpis. Integer viverra odio a arcu dictum dictum. In mattis turpis interdum vestibulum varius. Nunc placerat neque id justo pulvinar, a luctus neque tincidunt."))
        taskList.add(Task("task2","task2","Suspendisse bibendum et est sit amet elementum. Duis ligula ipsum, consectetur quis aliquet ac, volutpat eu tellus. Praesent ullamcorper quam a lacinia tempus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent feugiat sem vel sem iaculis venenatis. Quisque interdum in est vitae pellentesque. Maecenas tempus tincidunt eros, sit amet luctus diam porta in. Suspendisse eget mi convallis, porta libero quis, varius ante. Vivamus rutrum sapien nisl, nec iaculis urna molestie ut. Praesent molestie tempor consectetur."))
        taskList.add(Task("task3","task3","Proin vitae iaculis arcu. Pellentesque efficitur sem sit amet odio tempus fermentum. Nullam et lorem enim. Donec vel pellentesque nisi. Aenean nulla leo, volutpat vel scelerisque in, suscipit ut augue. Donec eget placerat felis. Quisque eleifend tempus leo, eu ullamcorper dolor pellentesque ac. Etiam eleifend metus quis augue dapibus placerat vel non velit. Nulla quis tortor ultricies, auctor orci eget, eleifend libero. Nam finibus efficitur dui, a venenatis ligula ultricies et. Vivamus bibendum aliquet eleifend. Nam enim ipsum, aliquam in urna nec, porttitor ullamcorper neque. Mauris vel porta arcu. Nulla arcu risus, placerat cursus ultrices aliquam, fringilla vel ipsum. Aliquam sodales nibh at massa tincidunt lobortis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;"))
    }
}