package com.example.test.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentTabCalendarAdapter(fm : FragmentManager, private val context : FragmentCalendar) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return FragmentCNote(context)
            }
            1 -> {
                return FragmentCTodolist(context)
            }
        }
        return FragmentCNote(context)
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            0 -> {
                return "Notes"
            }
            1 -> {
                return "To-Do List"
            }
        }
        return "Notes"
    }
}