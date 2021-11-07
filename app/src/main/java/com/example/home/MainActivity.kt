package com.example.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionText
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView

class MainActivity : AppCompatActivity() {

    var menu: Menu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mainToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.mainActivityToolbar)
        setSupportActionBar(mainToolbar)

        mainToolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSlider(savedInstanceState,mainToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    fun setSlider(savedInstanceState: Bundle?, mainToolbar: Toolbar) {
        val mainSlider = findViewById<MaterialDrawerSliderView>(R.id.mainSlider)

        Toast.makeText(this, "created slider", Toast.LENGTH_SHORT).show()

        mainSliderContent(mainSlider, savedInstanceState)
        mainSlider.onDrawerItemClickListener = { v, drawerItem, position ->
            var selected = when(drawerItem.identifier){
                0.toLong() -> "Home"
                1.toLong() -> "Profile"
                2.toLong() -> "Today"
                3.toLong() -> "Add"
                else -> "Nothing"
            }

            Toast.makeText(this, "${position}: $selected", Toast.LENGTH_SHORT).show()
            false
        }
        mainToolbar.setNavigationOnClickListener {
            mainSlider.drawerLayout?.open()
        }
    }

    private fun mainSliderContent(mainSlider: MaterialDrawerSliderView, savedInstanceState: Bundle?) {
        Toast.makeText(this, "setting slider content", Toast.LENGTH_SHORT).show()
        mainSlider.headerView = AccountHeaderView(this).apply {
            attachToSliderView(mainSlider) // attach to the slider
            addProfiles(
                ProfileDrawerItem().apply {
                    nameText = "Sarah A."
                    descriptionText = "sarah.matawah@gmail.com"
                    iconRes = R.drawable.ic_filter_24
                    identifier = 102
                }
            )
            onAccountHeaderListener = { view, profile, current ->
                // react to profile changes
                false
            }
            withSavedInstance(savedInstanceState)
        }

        val item = PrimaryDrawerItem().apply {
            nameRes = R.string.app_name
            iconRes = R.drawable.ic_add_24
            identifier = 0
        }
        val item1 = PrimaryDrawerItem().apply {
            identifier = 1
        }
        val item2 = PrimaryDrawerItem().apply {
            identifier = 2
        }
        val item3 = SecondaryDrawerItem().apply {
            identifier = 3
        }

        mainSlider.itemAdapter.add(
            item,
            DividerDrawerItem(),
            item1,
            item2,
            DividerDrawerItem(),
            item3
        )
    }
}