package com.example.test.todolist

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.setting.model.Category
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_to_do_list.view.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ToDoListFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private var listTasks = mutableListOf<Task>()
    private var listKey = mutableListOf<String>()
    private var listCategory = mutableListOf("Tất cả")

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mCateRef: DatabaseReference

    private var currentUserID = "xB4TZYstfHQGHaNWsDIJ49rVrvR2"

    private var calendar: Calendar? = null
    private var tvSelectedDay: TextView? = null
    private lateinit var selectedDayFromCalendar : String
    private var spinnerCate: Spinner? = null
    private var simpleDateFormat: SimpleDateFormat? = null

    private var lastSelectedYear = 0
    private var lastSelectedMonth = 0
    private var lastSelectedDay = 0
    private var view1: View? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        tvSelectedDay = view.findViewById(R.id.tv_selected_day)
        spinnerCate = view.findViewById(R.id.spn_cate_todolist)
        val rcvTasks = view.findViewById<RecyclerView>(R.id.rcv_todolist)
        val search = view.findViewById<SearchView>(R.id.search_view)

        selectedDayFromCalendar = arguments?.getString("selectedDay").toString()

        view.fab_add_task.setOnClickListener(this)
        view.button_back.setOnClickListener(this)
        view.button_next.setOnClickListener(this)
        tvSelectedDay!!.setOnClickListener(this)
        spinnerCate!!.onItemSelectedListener = this

        mDatabase = Firebase.database.getReference("tasks")
        mCateRef = Firebase.database.getReference("Category")

        getDefaultValue()

        rcvTasks.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(listTasks, listKey, view.context)
        rcvTasks.adapter = taskAdapter
        rcvTasks.addItemDecoration(DividerItemDecoration(rcvTasks.context, DividerItemDecoration.VERTICAL))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                taskAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                taskAdapter.filter.filter(newText)
                return false
            }
        })
        view1 = view
        return view
    }

    private fun getDefaultValue() {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tvSelectedDay!!.text = simpleDateFormat!!.format(calendar!!.time)

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, listCategory)
        spinnerCate?.adapter  = adapter

        mCateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (sn in snapshot.children) {
                    val category: Category? = sn.getValue(Category::class.java)
                    listCategory.add(category?.name_Cat!!)
                }
                for (i in listCategory) {
                    Log.d("TAG", "onDataChange: ${i}")
                }
                adapter.notifyDataSetChanged()
                getTasksFromDatabase()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getTasksFromDatabase() {
        val category: String = view?.findViewById<Spinner>(R.id.spn_cate_todolist)?.selectedItem.toString()
        val currentDateInTV = tvSelectedDay!!.text.toString()
        Log.d("=====TAG=====", "getTasksFromDatabase: $category $currentDateInTV")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTasks.clear()
                listKey.clear()
                for (sn in snapshot.children) {
                    val task = sn.getValue(Task::class.java)
                    val taskKey = sn.key
                    if (category == "Tất cả") {
                        if (task!!.uid == currentUserID && task.startDate == currentDateInTV) {
                            listTasks.add(task)
                            listKey.add(taskKey!!)
                        }
                    } else {
                        if (task!!.uid == currentUserID && task.startDate == currentDateInTV && task.cateogory == category) {
                            listTasks.add(task)
                            listKey.add(taskKey!!)
                        }
                    }
                }
                Log.d("======TAG======", "${listTasks.size}")
                taskAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab_add_task -> {
                startActivity(Intent(context, AddOrEditActivity::class.java))
            }
            R.id.button_next -> {
                nextDay()
            }
            R.id.button_back -> {
                previousDay()
            }
            R.id.tv_selected_day -> {
                initDate()
            }
        }
    }
    private fun initDate() {
        // Date Select Listener.
        val dateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

            calendar!!.set(year, monthOfYear, dayOfMonth)
            tvSelectedDay!!.text = simpleDateFormat.format(calendar!!.time)

            lastSelectedYear = year
            lastSelectedMonth = monthOfYear
            lastSelectedDay = dayOfMonth
        }

        val datePickerDialog = DatePickerDialog(
                view1!!.context,
                dateSetListener,
                lastSelectedYear,
                lastSelectedMonth,
                lastSelectedDay
        )
        datePickerDialog.show()
    }

    private fun previousDay() {
        val currentDateInTV = tvSelectedDay!!.text.toString()
        val date: Date = simpleDateFormat!!.parse(currentDateInTV)
        calendar!!.time = date
        calendar!!.add(Calendar.DAY_OF_MONTH, -1)
        tvSelectedDay!!.text = simpleDateFormat!!.format(calendar!!.time)
        getTasksFromDatabase()
    }

    private fun nextDay() {
        val currentDateInTV = tvSelectedDay!!.text.toString()
        val date: Date = simpleDateFormat!!.parse(currentDateInTV)
        calendar!!.time = date
        calendar!!.add(Calendar.DAY_OF_MONTH, +1)
        tvSelectedDay!!.text = simpleDateFormat!!.format(calendar!!.time)
        getTasksFromDatabase()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        getTasksFromDatabase()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onResume() {
        super.onResume()
        if(arguments?.getBoolean("isClickDetail") == true) {
            tvSelectedDay?.text = selectedDayFromCalendar
        }
    }
}