package com.example.home

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.home.Task.AddTaskFragment
import com.example.home.Task.EditTaskFragment
import com.example.home.Task.ViewTaskFragment
import com.example.home.Task.ViewTasksFragment
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

        setupToolbarAndSliderDrawer(savedInstanceState)

        //Frame layout that holds the fragment
        displayFragment(ViewTasksFragment())
    }

    private fun setupToolbarAndSliderDrawer(savedInstanceState: Bundle?) {
        var mainToolbar = findViewById<Toolbar>(R.id.mainActivityToolbar)
        setSupportActionBar(mainToolbar)

        mainToolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSlider(savedInstanceState, mainToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    private fun setSlider(savedInstanceState: Bundle?, mainToolbar: Toolbar) {
        val mainSlider = findViewById<MaterialDrawerSliderView>(R.id.mainSlider)

        Toast.makeText(this, "created slider", Toast.LENGTH_SHORT).show()

        mainSliderContent(mainSlider, savedInstanceState)
        mainSlider.onDrawerItemClickListener = { v, drawerItem, position ->
            when(drawerItem.identifier){
                0.toLong() -> displayFragment(ViewTasksFragment())
                1.toLong() -> displayFragment(ViewTaskFragment())
                2.toLong() -> displayFragment(AddTaskFragment())
                3.toLong() -> displayFragment(EditTaskFragment())
                else -> displayFragment(ViewTasksFragment())
            }
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
                false
            }
            withSavedInstance(savedInstanceState)
        }

        val item = PrimaryDrawerItem().apply {
            nameRes = R.string.home
            iconRes = R.drawable.ic_home_24
            identifier = 0
        }
        val item1 = PrimaryDrawerItem().apply {
            nameRes = R.string.profile
            iconRes = R.drawable.ic_id_card_24
            identifier = 1
        }
        val item2 = PrimaryDrawerItem().apply {
            nameRes = R.string.today
            iconRes = R.drawable.ic_today_24
            identifier = 2
        }
        val item3 = SecondaryDrawerItem().apply {
            nameRes = R.string.setting
            iconRes = R.drawable.ic_settings_24
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

    fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.task_fragment_frame, fragment).commit()
    }
}