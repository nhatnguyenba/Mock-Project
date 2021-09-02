package com.example.test.calendar.notes

interface INote {
    fun updateNote(id : String, name : String)
    fun deleteNote(id : String)
}