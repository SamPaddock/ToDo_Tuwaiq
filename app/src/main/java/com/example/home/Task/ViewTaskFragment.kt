package com.example.home.Task

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.home.Model.Task
import com.example.home.R

class ViewTaskFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_view_task, container, false)

        setHasOptionsMenu(true)

        var data = arguments?.getSerializable("task") as Task

        setData(inflate,data)

        return inflate
    }

    fun setData(inflate: View, data: Task?) {
        if (data != null) {
            inflate.findViewById<TextView>(R.id.textViewViewTaskTitle).text = data.title
            inflate.findViewById<TextView>(R.id.textViewViewTaskDetails).text = data.desciption
            inflate.findViewById<TextView>(R.id.textViewViewDueDate).text = data.dueDate.toString()
            inflate.findViewById<ImageView>(R.id.imageViewViewMemberTask).setImageResource(R.mipmap.ic_app_icon)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_task_menu,menu)
        super.onCreateOptionsMenu(menu,inflater)
    }
}