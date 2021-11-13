package com.example.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.home.Launcher.Splash
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
                1.toLong() -> displayFragment(ViewTasksFragment(),"all")
                2.toLong() -> displayFragment(ViewTasksFragment(),"today")
                3.toLong() -> displayFragment(ViewTasksFragment(),"pastDue")
                4.toLong() -> displayFragment(ViewTasksFragment(),"completed")
                7.toLong() -> signOutFromAccount()
                else -> displayFragment(ViewTasksFragment(),"home")
            }
            false
        }
        mainToolbar.setNavigationOnClickListener {
            mainSlider.drawerLayout?.open()
        }
    }

    private fun mainSliderContent(mainSlider: MaterialDrawerSliderView, savedInstance: Bundle?) {
        mainSliderHeader(mainSlider, savedInstance)

        val item = PrimaryDrawerItem().apply {
            nameRes = R.string.home
            iconRes = R.drawable.ic_home_24
            identifier = 0
        }
        val item1 = PrimaryDrawerItem().apply {
            nameRes = R.string.all_tasks
            iconRes = R.drawable.ic_list_alt_24
            identifier = 1
        }
        val item2 = PrimaryDrawerItem().apply {
            nameRes = R.string.today
            iconRes = R.drawable.ic_today_24
            identifier = 2
        }
        val item3 = PrimaryDrawerItem().apply {
            nameRes = R.string.past_due
            iconRes = R.drawable.ic_assignment_late_24
            identifier = 3
        }
        val item4 = PrimaryDrawerItem().apply {
            nameRes = R.string.completed_tasks
            iconRes = R.drawable.ic_done_all_24
            identifier = 4
        }
        val item7 = SecondaryDrawerItem().apply {
            nameRes = R.string.sign_out_btn
            iconRes = R.drawable.ic_exit_24
            identifier = 7
        }

        mainSlider.itemAdapter.add(
            item,
            DividerDrawerItem(),
            item1,
            item2,
            item3,
            item4,
            DividerDrawerItem(),
            item7
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

    private fun signOutFromAccount(){
        Firebase.auth.signOut()
        val intent = Intent(this, Splash::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayFragment(fragment: Fragment, tag: String) {
        val bundle = Bundle()
        bundle.putString("filterType", tag)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.task_fragment_frame, fragment).commit()
    }
}