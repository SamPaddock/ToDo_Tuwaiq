package com.example.home.Task

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.home.R

class EditTaskFragment : Fragment() {
    //TODO: edit depending on dueDate statues
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_edit_task, container, false)

        setHasOptionsMenu(true)

        return inflate
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu,inflater)
    }
}