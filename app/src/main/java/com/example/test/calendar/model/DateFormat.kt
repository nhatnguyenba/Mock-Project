package com.example.test.calendar.model

class DateFormat(val date : String?, private val monthYear : String?) {

    override fun toString(): String {
        return "$date/$monthYear"
    }
}