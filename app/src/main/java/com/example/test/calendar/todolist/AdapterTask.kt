package com.example.test.calendar.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R

class AdapterTask : RecyclerView.Adapter<AdapterTask.NoteViewHolder>(){

    private lateinit var listTask : List<String>

    fun setData(listT : List<String>) {
        this.listTask = listT
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvNameTask : TextView = itemView.findViewById(R.id.tvNameTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_task, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val taskName = listTask[position]
        holder.tvNameTask.text = taskName
    }

    override fun getItemCount(): Int {
        return listTask.size
    }


}