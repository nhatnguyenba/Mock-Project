package com.example.test.statistic

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun compare(date1: String, date2: String): Int {
            val date1ListInt = convertStringToList(date1)
            val date2ListInt = convertStringToList(date2)

            when {
                date1ListInt[2] > date2ListInt[2] -> return 1
                date1ListInt[2] < date2ListInt[2] -> return -1
                else -> {
                    when {
                        date1ListInt[1] > date2ListInt[1] -> return 1
                        date1ListInt[1] < date2ListInt[1] -> return -1
                        else -> {
                            if (date1ListInt[0] > date2ListInt[0])
                                return 1
                            return if (date1ListInt[0] < date2ListInt[0])
                                -1
                            else
                                0
                        }
                    }
                }
            }
        }

        fun convertDateToString(date: Date): String {
            val format = SimpleDateFormat("dd/MM/yyy")
            return format.format(date)
        }

        private fun convertStringToList(date: String): MutableList<Int> {
            val dateListString = date.split("/").toMutableList()
            val dateListInt = mutableListOf<Int>()

            for (num in dateListString) {
                dateListInt.add(num.toInt())
            }
            return dateListInt
        }

    }
}
