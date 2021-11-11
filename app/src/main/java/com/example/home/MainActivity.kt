package com.example.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.home.Model.User
import com.example.home.Task.ViewTasksFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbarAndSliderDrawer(savedInstanceState)

        displayFragment(ViewTasksFragment(),"home")
    }

    private fun setupToolbarAndSliderDrawer(savedInstanceState: Bundle?) {
        val mainToolbar = findViewById<Toolbar>(R.id.mainActivityToolbar)
        setSupportActionBar(mainToolbar)

        mainToolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSlider(savedInstanceState, mainToolbar)
    }

    private fun setSlider(savedInstanceState: Bundle?, mainToolbar: Toolbar) {
        val mainSlider = findViewById<MaterialDrawerSliderView>(R.id.mainSlider)

        mainSliderContent(mainSlider, savedInstanceState)
        mainSlider.onDrawerItemClickListener = { v, drawerItem, position ->
            when(drawerItem.identifier){
                0.toLong() -> displayFragment(ViewTasksFragment(),"home")
                1.toLong() -> displayFragment(ViewTasksFragment(),"display")
                2.toLong() -> displayFragment(ViewTasksFragment(),"add")
                3.toLong() -> displayFragment(ViewTasksFragment(),"edit")
                else -> displayFragment(ViewTasksFragment(),"home")
            }
            false
        }
        mainToolbar.setNavigationOnClickListener {
            mainSlider.drawerLayout?.open()
        }
    }

    private fun mainSliderContent(mainSlider: MaterialDrawerSliderView, savedInstance: Bundle?) {
        //TODO: Custom list
        //TODO: Click custom list opens ViewTasks with filter option
        mainSliderHeader(mainSlider, savedInstance)

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

    private fun mainSliderHeader(mainSlider: MaterialDrawerSliderView, savedInstanceState: Bundle?){
        val currentUser = Firebase.auth.currentUser
        mainSlider.headerView = AccountHeaderView(this).apply {
            attachToSliderView(mainSlider) // attach to the slider
            addProfiles(
                ProfileDrawerItem().apply {
                    descriptionText = currentUser?.email ?: ""
                    identifier = 102
                }
            )
            onAccountHeaderListener = { view, profile, current ->
                false
            }
            withSavedInstance(savedInstanceState)
        }
    }

    fun displayFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.task_fragment_frame, fragment).addToBackStack(tag).commit()
    }
}