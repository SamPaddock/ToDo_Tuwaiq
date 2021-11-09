package com.example.home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.home.Model.User
import com.example.home.R

class UserDropDownAdapter (var context: Context, var data: MutableList<User>): BaseAdapter(), Filterable {

    var userList = data

    override fun getCount() = data.size

    override fun getItem(position: Int) = data[position]


    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_dropdown_row_layout,parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewUserImageDropdown)
        val name = view.findViewById<TextView>(R.id.textViewUserNameDropdown)

        imageView.setImageResource(R.mipmap.ic_app_icon)
        name.text = data[position].fullname
        println("getView ${ data[position].fullname }")

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_dropdown_row_layout,parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewUserImageDropdown)
        val name = view.findViewById<TextView>(R.id.textViewUserNameDropdown)

        imageView.setImageResource(R.mipmap.ic_app_icon)
        name.text = data[position].fullname
        println("getDropDownView ${ data[position].fullname }")

        return view
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase()
                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    userList
                else
                    userList.filter {it.fullname!!.contains(queryString)}
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    userList = results.values as MutableList<User>
                    notifyDataSetChanged()
                }
            }

        }
    }

}