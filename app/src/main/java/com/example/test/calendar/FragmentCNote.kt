package com.example.test.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.calendar.notes.AdapterNote
import com.example.test.calendar.notes.INote
import com.example.test.calendar.notes.Note
import com.google.firebase.database.*

class FragmentCNote(private val context : FragmentCalendar) : Fragment(), INote {

    private lateinit var database : DatabaseReference
    private lateinit var listNote : ArrayList<Note>
    private lateinit var adapterNote: AdapterNote
    private lateinit var rvNote : RecyclerView

    private lateinit var btnAddNote : ImageView

    private val iCalendar : ICalendar = context

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_c_note, container, false)
        database = FirebaseDatabase.getInstance().getReference("notes")
        listNote = ArrayList()
        adapterNote = AdapterNote(this)
        rvNote = view.findViewById(R.id.rvNote)
        rvNote.adapter = adapterNote
        val layoutManager = LinearLayoutManager(getContext())
        rvNote.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            rvNote.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext()!!, R.drawable.custom_divider)!!)
        rvNote.addItemDecoration(dividerItemDecoration)

        adapterNote.setData(listNote)

        btnAddNote = view.findViewById(R.id.btnAddNote)
        btnAddNote.setOnClickListener {
            val alertDialog = AlertDialog.Builder(getContext()!!)
            val textEditText = EditText(getContext())
            alertDialog.setMessage("Enter note")
            alertDialog.setTitle("Add note: ")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add") { dialog, _ ->
                val note = Note()
                note.name = textEditText.text.toString()
                note.day = iCalendar.getData()

                Toast.makeText(getContext(), note.day.toString(), Toast.LENGTH_SHORT).show()

                val newItemData = database.push()
                note.id = newItemData.key

                newItemData.setValue(note)

                dialog.dismiss()
                adapterNote.setData(listNote)
                Toast.makeText(getContext(), note.day.toString(), Toast.LENGTH_SHORT).show()
            }
            alertDialog.show()
        }

        getDatabase()
        context.tvChooseDay.addTextChangedListener {
            getDatabase()
        }

        return view
    }

    override fun updateNote(id: String, name: String) {
        database.child(id).child("name").setValue(name)
        adapterNote.setData(listNote)
    }

    override fun deleteNote(id: String) {
        database.child(id).removeValue()
        adapterNote.setData(listNote)
    }

    private fun getDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listNote.clear()
                for (dataSnapShot in snapshot.children) {
                    val work = dataSnapShot.getValue(Note::class.java)
                    if(work?.day == iCalendar.getData()) {
                        listNote.add(work)
                    }
                }
                adapterNote.setData(listNote)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}