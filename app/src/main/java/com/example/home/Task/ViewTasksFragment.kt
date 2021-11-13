package com.example.home.Task

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.home.Adapter.TaskAdapter
import com.example.home.Firebase.FirebaseClient
import com.example.home.MainActivity
import com.example.home.Model.TaskDataModel
import com.example.home.Model.TaskModel
import com.example.home.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ViewTasksFragment : Fragment() {

    var taskList = mutableListOf<TaskDataModel>()
    lateinit var inflate: View
    lateinit var adapter: TaskAdapter
    lateinit var recycledView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_view_tasks, container, false)


        setHasOptionsMenu(true)

        val addTaskBtn = inflate.findViewById<FloatingActionButton>(R.id.fabAddTask)

        addTaskBtn.setOnClickListener {
            val baseView = context as MainActivity
            val intent = Intent(baseView,AddTaskActivity::class.java)
            baseView.startActivity(intent)
        }

        return inflate
    }

    override fun onStart() {
        taskList.removeAll(taskList)

        FirebaseClient().getAllTasks(Firebase.auth.uid!!).observe(viewLifecycleOwner,{
            taskList = it
            recycledView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
            recycledView.layoutManager = LinearLayoutManager(activity)
            adapter = context?.let { TaskAdapter(this.viewLifecycleOwner, it, taskList) }!!
            recycledView.adapter = adapter
            filterFromSlider(taskList)
        })

        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        val searchItem = menu.findItem(R.id.home_search_view)
        val searchView = searchItem?.actionView as SearchView
        searchTask(inflate.findViewById<RecyclerView>(R.id.recyclerView), searchView)

        sortList(menu)

        super.onCreateOptionsMenu(menu,inflater)
    }

    private fun filterCustomDialog(filterlayout: Int): Pair<AlertDialog, View> {
        val customAlertDialog = AlertDialog.Builder(this.context).create()
        customAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = layoutInflater.inflate(filterlayout, null)
        return Pair(customAlertDialog, view)
    }

    private fun sortList(menu: Menu) {
        menu.findItem(R.id.alphaAscSort).setOnMenuItemClickListener {
            taskList.sortBy { it.title }
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it,
                taskList)}
            true
        }
        menu.findItem(R.id.alphaDescSort).setOnMenuItemClickListener {
            taskList.sortByDescending { it.title }
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it,
                taskList)}
            true
        }
        menu.findItem(R.id.dateAddedAscSort).setOnMenuItemClickListener {
            taskList.sortByDescending { it.creationDate }
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it,
                taskList)}
            true
        }
    }

    private fun filterFromSlider(mutableList: MutableList<TaskDataModel>){
        val filterType = arguments?.get("filterType")
        val result: List<TaskDataModel>
        if (filterType == "home"){
            result = mutableList.filter { (it.isDone != true) }
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it,
                result as MutableList<TaskDataModel>)}
        } else if (filterType == "today"){
            result = mutableList.filter {(TaskModel().remainingDays(it.dueDate) == 0
                    && !TaskModel().isPastDue(it.dueDate))}
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it,
                result as MutableList<TaskDataModel>)}
        } else {
            recycledView.adapter = context?.let {TaskAdapter(
                this@ViewTasksFragment.viewLifecycleOwner, it, mutableList)}
        }
    }

    fun searchTask(recycledView: RecyclerView, searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val newData = taskList.filter { (it.title.lowercase().contains(query!!.lowercase())) }
                recycledView.adapter = context?.let {TaskAdapter(
                    this@ViewTasksFragment.viewLifecycleOwner, it,
                    newData as MutableList<TaskDataModel>)}
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newData = taskList.filter { (it.title.lowercase().contains(newText!!.lowercase())) }
                recycledView.adapter = context?.let {TaskAdapter(
                    this@ViewTasksFragment.viewLifecycleOwner, it,
                    newData as MutableList<TaskDataModel>)}
                return true
            }
        })
    }

}
