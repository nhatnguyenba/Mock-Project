package com.example.test.todolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.todolist_item.view.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TaskAdapter(
        private var listTasks: MutableList<Task>,
        var listKey: MutableList<String>,
        val context: Context
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(), View.OnClickListener, Filterable {

    private var listTasksFull: MutableList<Task> = listTasks

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.tv_title
        var tvContent: TextView = itemView.tv_content
        var tvTimeAndCateg: TextView = itemView.tv_category
        var deleteItem: ImageButton = itemView.imgbutton_delete
        var editItem: ImageButton = itemView.imgbutton_edit
        var statusItem: CheckBox = itemView.cb_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = listTasks[position]
        val taskKey = listKey[position]
        holder.tvTitle.text = task.title
        holder.tvContent.text = task.content
        holder.tvTimeAndCateg.text = task.cateogory + " - end at:" + task.endDate
        holder.statusItem.isChecked = task.done!!

//        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
//        val calendar = Calendar.getInstance()
//        val currentDate: Date = calendar.time
//        val taskDate = simpleDateFormat.parse(task.startDate)
//        Log.d("TAG", "onBindViewHolder: $currentDate - $taskDate - ${task.startDate}")
//
//        if(taskDate >= currentDate){
//            holder.editItem.isEnabled = true
//            holder.statusItem.isEnabled = true
//        }else{
//            holder.editItem.isEnabled = false
//            holder.statusItem.isEnabled = false
//        }

        holder.editItem.setOnClickListener {
            Intent(context, AddOrEditActivity::class.java).also {
                val bundle = Bundle()
                bundle.putSerializable("task", task)
                bundle.putString("task_key", taskKey)

                it.putExtras(bundle)
                context.startActivity(it)
            }
        }
        holder.deleteItem.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Confirm!!!")
            alertDialog.setMessage("Are you sure?")
            alertDialog.setPositiveButton("YES") { _, _ ->
                Firebase.database.getReference("tasks").child(taskKey).removeValue()
            }
            alertDialog.setNegativeButton("NO"){ dialog, _ ->
                dialog.dismiss()
            }.create().show()
        }
        holder.statusItem.setOnClickListener {
            val database = Firebase.database.getReference("tasks")
            if(task.done == false){
                database.child(taskKey).child("done").setValue(true)
            }
            else{
                database.child(taskKey).child("done").setValue(false)
            }
            holder.statusItem.isChecked = task.done!!
        }

    }

    override fun getItemCount(): Int {
        return listTasks.size
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imgbutton_edit -> {
                Intent(context, AddOrEditActivity::class.java).also {
                    context.startActivity(it)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val strSearch = constraint.toString().toLowerCase()
                Log.d("TAG", "performFiltering: ${listTasksFull.size}")
                if(strSearch.isEmpty()){
                    listTasks = listTasksFull
                }else{
                    val tmpList = mutableListOf<Task>()
                    for (task in listTasksFull){
                        if(task.title!!.toLowerCase().contains(strSearch)){
                            tmpList.add(task)
                        }
                    }
                    listTasks = tmpList
                }
                val filterResults = FilterResults()
                filterResults.values = listTasks
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listTasks = results!!.values as MutableList<Task>
                notifyDataSetChanged()
            }
        }
    }
}