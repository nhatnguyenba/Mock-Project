package com.example.test

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.test.calendar.FragmentCalendar
import com.example.test.calendar.IMainActivity
import com.example.test.calendar.timer.FragmentTimer
import com.example.test.news.NewsFragment
import com.example.test.setting.IDarkMode
import com.example.test.setting.SettingsFragment
import com.example.test.statistic.ChartFragment
import com.example.test.statistic.StatisticDao
import com.example.test.todolist.ToDoListFragment
import com.example.test.statistic.FiltersFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    IDarkMode, IMainActivity {

    private val FRAGMENT_TODOLIST = 1
    private val FRAGMENT_SETTING = 2
    private val FRAGMENT_FILTERS = 3
    private val FRAGMENT_CHART = 4
    private val FRAGMENT_CALENDAR = 5
    private val FRAGMENT_TIMER = 6
    private val FRAGMENT_NEWS = 7

    private var currentFragment = FRAGMENT_TODOLIST

    private lateinit var statisticDao: StatisticDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //listen to changed data event
        statisticDao = StatisticDao.getInstance(this)
        statisticDao.registerAddValueEventListener()

        if (this.getSharedPreferences(
                "com.example.hn21_cpl_android_03g2_preferences",
                Context.MODE_PRIVATE
            ).getBoolean("enable_dark_mode", false)
        ) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        toolbar.tv_toolbar_title.text = "To Do List"
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        replaceFragment(ToDoListFragment())
        navigationView.setCheckedItem(R.id.navToDoList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navToDoList -> {
                if (currentFragment != FRAGMENT_TODOLIST) {
                    replaceFragment(ToDoListFragment())
                    currentFragment = FRAGMENT_TODOLIST
                    toolbar.tv_toolbar_title.text = "To Do List"
                }
            }
            R.id.navSetting -> {
                if (currentFragment != FRAGMENT_SETTING) {
                    replaceFragment(SettingsFragment())
                    currentFragment = FRAGMENT_SETTING
                    toolbar.tv_toolbar_title.text = "Setting"
                }
            }
            R.id.navFilters -> {
                if (currentFragment != FRAGMENT_FILTERS) {
                    replaceFragment(FiltersFragment())
                    currentFragment = FRAGMENT_FILTERS
                    toolbar.tv_toolbar_title.text = "Filters"
                }
            }
            R.id.navChart -> {
                if (currentFragment != FRAGMENT_CHART) {
                    replaceFragment(ChartFragment())
                    currentFragment = FRAGMENT_CHART
                    toolbar.tv_toolbar_title.text = "Chart"
                }
            }
            R.id.navCalendar -> {
                if (currentFragment != FRAGMENT_CALENDAR) {
                    replaceFragment(FragmentCalendar())
                    currentFragment = FRAGMENT_CALENDAR
                    toolbar.tv_toolbar_title.text = "Calendar"
                }
            }
            R.id.navTimer -> {
                if (currentFragment != FRAGMENT_TIMER) {
                    replaceFragment(FragmentTimer())
                    currentFragment = FRAGMENT_TIMER
                    toolbar.tv_toolbar_title.text = "Timer"
                }
            }
            R.id.navNews -> {
                if (currentFragment != FRAGMENT_NEWS) {
                    replaceFragment(NewsFragment())
                    currentFragment = FRAGMENT_NEWS
                    toolbar.tv_toolbar_title.text = "News"
                }
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun goToFragmentToDoList(selectedDay: String, isClickDetail: Boolean) {
        if (currentFragment != FRAGMENT_TODOLIST) {
            val bundle = Bundle()
            bundle.putString("selectedDay", selectedDay)
            bundle.putBoolean("isClickDetail", isClickDetail)
            val toDoListFragment = ToDoListFragment()
            toDoListFragment.arguments = bundle
            replaceFragment(toDoListFragment)
            currentFragment = FRAGMENT_TODOLIST
            toolbar.tv_toolbar_title.text = "To Do List"
            navigationView.setCheckedItem(R.id.navToDoList)
        }
    }

    override fun processDarkMode() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}