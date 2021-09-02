package com.example.test.calendar.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.test.calendar.FragmentCNote
import com.example.test.R

class AdapterNote(context : FragmentCNote) : RecyclerView.Adapter<AdapterNote.NoteViewHolder>(){

    private lateinit var listNote : List<Note>
    private val iNote = context

    fun setData(listN : List<Note>) {
        this.listNote = listN
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvNameNote : TextView = itemView.findViewById(R.id.tvNameNote)
        val btnDeleteNote : ImageButton = itemView.findViewById(R.id.btnDeleteNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = listNote[position]
        holder.tvNameNote.text = note.name

        holder.tvNameNote.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            val textEditText = EditText(holder.itemView.context)
            textEditText.setText(note.name)
            alertDialog.setMessage("Enter note")
            alertDialog.setTitle("Edit note: ")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Edit") { dialog, _ ->
                iNote.updateNote(note.id as String, textEditText.text.toString())
                dialog.dismiss()
            }
            alertDialog.show()
        }
        holder.btnDeleteNote.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setMessage("Are you sure?")
            alertDialog.setTitle("Delete note")
            alertDialog.setPositiveButton("Yes") {dialog, _ ->
                iNote.deleteNote(note.id as String)
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("No") { _, _ ->

            }
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }


}