package com.example.test.statistic

import android.content.Context
import android.content.SharedPreferences
import com.example.test.todolist.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class StatisticDao {

    private lateinit var context: Context
    private val dbRef = Firebase.database.reference
    lateinit var sharedPreferences: SharedPreferences

    private var tasksIsToday = mutableListOf<Task>()
    private var tasksIsOverdue = mutableListOf<Task>()
    private var tasksIsInTime = mutableListOf<Task>()
    private var tasksIsCompleted = mutableListOf<Task>()

    companion object {
        private var INSTANCE: StatisticDao? = null
        fun getInstance(context: Context): StatisticDao {
            if (INSTANCE == null) {
                INSTANCE = StatisticDao()
                INSTANCE!!.context = context
                INSTANCE!!.sharedPreferences = context.getSharedPreferences(
                    "statistics",
                    Context.MODE_PRIVATE
                )
            }
            return INSTANCE!!
        }
    }

    fun registerAddValueEventListener() {
        registerForDoneTasks()
        registerForInProgressTasks()
        registerForOverdueTasks()
    }

    private fun registerForDoneTasks() {
        val query = dbRef.database.reference
            .child("tasks")
            .orderByChild("done")
            .equalTo(true)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val editor = sharedPreferences.edit()
                editor.putLong("numberOfTasksIsDone", snapshot.childrenCount)
                    .commit()

                for (sn in snapshot.children) {
                    sn.getValue<Task>()?.let { tasksIsCompleted.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun registerForInProgressTasks() {
        val query = dbRef.database.reference
            .child("tasks")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //in progress = all - (done + overdue) and save into shared preferences
                val all = snapshot.childrenCount
                val done = sharedPreferences.getLong("numberOfTasksIsDone", 0)
                val overdue = sharedPreferences.getLong("numberOfTasksIsOverdue", 0)
                val inProgress = all - (done + overdue)

                sharedPreferences.edit()
                    .putLong("numberOfTasksIsInProgress", inProgress)
                    .commit()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun registerForOverdueTasks() {
        val query = dbRef.database.reference
            .child("tasks")
            .orderByChild("done")
            .equalTo(false)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0L
                for (sn in snapshot.children) {
                    val task = sn.getValue<Task>()
                    val startDate = task?.startDate
                    val currentDate = DateUtils.convertDateToString(Date())
                    if (startDate != null && DateUtils.compare(currentDate, startDate) > 0) {
                        ++count
                        tasksIsOverdue.add(task)
                    }

                    if (startDate != null && DateUtils.compare(currentDate, startDate) == 0) {
                        tasksIsToday.add(task)
                    }

                    if (startDate == null) {
                        task?.let { tasksIsInTime.add(it) }
                    }

                    if (startDate != null && DateUtils.compare(currentDate, startDate) <= 0) {
                        tasksIsInTime.add(task)
                    }
                }
                sharedPreferences.edit()
                    .putLong("numberOfTasksIsOverdue", count)
                    .commit()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getTasksIsToday(): MutableList<Task> {
        return tasksIsToday
    }

    fun getTasksIsOverdue(): MutableList<Task> {
        return tasksIsOverdue
    }

    fun getTasksIsInTime(): MutableList<Task> {
        return tasksIsInTime
    }

    fun getTasksIsCompleted(): MutableList<Task> {
        return tasksIsCompleted
    }

}
