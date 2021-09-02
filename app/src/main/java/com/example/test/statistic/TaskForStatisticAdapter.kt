package com.example.test.statistic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.statistic.TaskForStatisticAdapter.TaskForStatisticViewHolder
import com.example.test.todolist.Task

class TaskForStatisticAdapter(private val context: Context, private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskForStatisticViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskForStatisticViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.task_for_statistic_item, parent, false)
        return TaskForStatisticViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskForStatisticViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.content.text = task.content
        holder.category.text = task.cateogory
        holder.startDate.text = "End on: ".plus(task.startDate)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskForStatisticViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.statistic_title_txt)
        var content: TextView = itemView.findViewById(R.id.statistic_content_txt)
        var category: TextView = itemView.findViewById(R.id.statistic_category_txt)
        var startDate: TextView = itemView.findViewById(R.id.statistic_startDate_txt)

    }
}
