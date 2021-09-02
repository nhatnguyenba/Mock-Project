package com.example.test.calendar

import java.time.LocalDate

interface ICalendar {
    fun dateMonthYearFromDate(date : LocalDate) : String
    fun setData(text : String)
    fun getData() : String
}