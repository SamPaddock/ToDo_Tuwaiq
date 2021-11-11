package com.example.home.Adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import com.example.home.Firebase.FirebaseClient
import com.example.home.Model.Task
import com.example.home.Model.User
import com.example.home.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class UserDropDownAdapter(var context: Context, var data: MutableList<User>): BaseAdapter(), Filterable {

    var userList = data

    override fun getCount() = data.size

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_dropdown_row_layout,parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewUserImageDropdown)
        val name = view.findViewById<TextView>(R.id.textViewUserNameDropdown)

        name.text = data[position].fullname

        Firebase.storage.getReferenceFromUrl( data[position].imageUri).downloadUrl
            .addOnCompleteListener {
                if (it.isSuccessful){
                    data[position].imageDataUri = it.result.toString()
                    Picasso.get().load(Uri.parse(it.result.toString())).fit().into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.ic_name_24)
                }
            }.addOnFailureListener {
                if (parent != null) {
                    Snackbar.make(context, parent.rootView,"${it.message}", 6000)
                } else{
                    Log.d(TAG,"Error: ${it.message}")
                }
            }
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