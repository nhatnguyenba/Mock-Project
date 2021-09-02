package com.example.test.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.calendar.todolist.AdapterTask
import com.example.test.calendar.todolist.Task
import com.google.firebase.database.*

class FragmentCTodolist(private val context : FragmentCalendar) : Fragment() {

    var finishCount = 0
    var unFinishCount = 0

    var isClickDetail = false

    private val iCalendar : ICalendar = context
    private var iMainActivity : IMainActivity? = null

    private lateinit var database : DatabaseReference
    private lateinit var listTask : ArrayList<String>
    private lateinit var adapterTask: AdapterTask
    private lateinit var rvTask : RecyclerView
    private lateinit var tvToDoList : TextView
    private lateinit var btnDetailToDoList : Button

    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_c_todolist, container, false)
        rvTask = view.findViewById(R.id.rvTask)
        tvToDoList = view.findViewById(R.id.tvToDoList)
        btnDetailToDoList = view.findViewById(R.id.btnDetailToDoList)
        database = FirebaseDatabase.getInstance().getReference("tasks")
        listTask = ArrayList()
        adapterTask = AdapterTask()
        rvTask.adapter = adapterTask
        val layoutManager = LinearLayoutManager(getContext())
        rvTask.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rvTask.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext()!!, R.drawable.custom_divider)!!)
        rvTask.addItemDecoration(dividerItemDecoration)
        adapterTask.setData(listTask)

        getDatabase()
        context.tvChooseDay.addTextChangedListener {
            getDatabase()
        }

        iMainActivity = activity as IMainActivity
        btnDetailToDoList.setOnClickListener {
            isClickDetail = true
            iMainActivity?.goToFragmentToDoList(iCalendar.getData(), isClickDetail)
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun getDatabase() {
        finishCount = 0
        unFinishCount = 0
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTask.clear()
                for (dataSnapShot in snapshot.children) {
                    val task = dataSnapShot.getValue(Task::class.java)
                    if(task?.startDate == iCalendar.getData()) {
                        listTask.add(task.title.toString())
                        if(task.done == true) {
                            finishCount++
                        } else if(task.done == false) {
                            unFinishCount++
                        }
                    }
                }
                adapterTask.setData(listTask)
                tvToDoList.text = "$finishCount finished/ $unFinishCount unfinished"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}