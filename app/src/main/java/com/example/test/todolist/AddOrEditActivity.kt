package com.example.test.todolist

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.setting.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_or_edit.*
import java.text.SimpleDateFormat
import java.util.*


class AddOrEditActivity : AppCompatActivity(), View.OnClickListener {
    private var datePickerDialog: DatePickerDialog? = null
    private var timePickerDialog: TimePickerDialog? = null

    private var calendar: Calendar? = null
    private var simpleDateFormat: SimpleDateFormat? = null
    private var lastSelectedYear = 0
    private var lastSelectedMonth = 0
    private var lastSelectedDay = 0

    private var lastSelectedHour = 0
    private var lastSelectedMinute = 0

    private var spinnerAdd: Spinner? = null

    private var listCategory = mutableListOf<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var mdatabase: DatabaseReference
    private var taskKey:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit)
        spinnerAdd = findViewById(R.id.spn_category)

        tv_date.setOnClickListener(this)
        tv_time.setOnClickListener(this)
        imgbutton_close.setOnClickListener(this)
        tv_save.setOnClickListener(this)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //get values of category
        val mDatabase = Firebase.database.reference
        mDatabase.child("Category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                for (sn in snapshot.children) {
//                    listCategory.add(sn.value.toString())
//                }

                for (sn in snapshot.children) {
                    val category: Category? = sn.getValue(Category::class.java)
                    listCategory.add(category?.name_Cat!!)
                }

                spinnerAdd!!.adapter = arrayAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        getDefaultInfo()

        mdatabase = Firebase.database.reference
    }
    private fun getIndex(spinner: Spinner, myString: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                Log.d("HungND", "getIndex: $myString $i")
                return i
            }
        }
        return -1
    }

    private fun getDefaultInfo() {
        // get current date and time
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tv_date.text = simpleDateFormat!!.format(calendar!!.time)

        simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        tv_time.text = simpleDateFormat!!.format(calendar!!.time)

        // get values of current task to update
        val intent = intent
        if(intent!= null){
            val bundle = intent.extras
            if(bundle != null){
                taskKey = intent.getStringExtra("task_key")

                val task:Task = intent.getSerializableExtra("task") as Task
                edt_title.setText(task.title)
                edt_content.setText(task.content)
                tv_date.text = task.startDate
                tv_time.text = task.endDate

//                val index = arrayAdapter.getPosition(task.cateogory)
//                Log.d("HungND", "getDefaultInfo: ${task.cateogory} $index")
//                spinnerAdd!!.setSelection(index)
                spinnerAdd!!.setSelection(getIndex(spn_category, task.cateogory))
            }
        }

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initTime() {
        // Time Set Listener.
        val timeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            calendar!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar!!.set(Calendar.MINUTE, minute)
            tv_time.text = simpleDateFormat.format(calendar!!.time)
            lastSelectedHour = hourOfDay
            lastSelectedMinute = minute
        }

        timePickerDialog = TimePickerDialog(
                this,
                timeSetListener, lastSelectedHour, lastSelectedMinute, true
        )
        timePickerDialog!!.show()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initDate() {
        // Date Select Listener.
        val dateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

            calendar!!.set(year, monthOfYear, dayOfMonth)
            tv_date.text = simpleDateFormat.format(calendar!!.time)

            lastSelectedYear = year
            lastSelectedMonth = monthOfYear
            lastSelectedDay = dayOfMonth
        }

        datePickerDialog = DatePickerDialog(
                this,
                dateSetListener,
                lastSelectedYear,
                lastSelectedMonth,
                lastSelectedDay
        )
        datePickerDialog!!.datePicker.minDate = System.currentTimeMillis()-1000
        datePickerDialog!!.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_date -> {
                initDate()
            }
            R.id.tv_time -> {
                initTime()
            }
            R.id.imgbutton_close -> {
                finish()
            }
            R.id.tv_save -> {
                saveTaskToDatabase()
            }
        }
    }

    private fun saveTaskToDatabase() {
        simpleDateFormat = SimpleDateFormat("hh::mm:ss", Locale.getDefault())
        val currentTime = simpleDateFormat!!.format(calendar!!.time).toString()

        val title = edt_title.text.toString()
        val content = edt_content.text.toString()
        val startDate = tv_date.text.toString()
        val endTime = tv_time.text.toString()
        val category = spn_category.selectedItem.toString()
        val uid = "xB4TZYstfHQGHaNWsDIJ49rVrvR2"
        val task = Task(title as String?, content as String?, category as String?,
                startDate as String?, endTime as String?, false, uid)

        when {
            TextUtils.isEmpty(title) -> {
                Toast.makeText(this, "Write something to title", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(content) -> {
                Toast.makeText(this, "Write something to content", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if(taskKey==null){
                    mdatabase.child("tasks").child(uid + currentTime).setValue(task).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Add successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    mdatabase.child("tasks").child(taskKey!!).setValue(task).addOnCompleteListener {
                        if(it.isSuccessful)
                            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}