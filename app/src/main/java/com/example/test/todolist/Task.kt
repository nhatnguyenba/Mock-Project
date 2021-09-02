package com.example.test.todolist

import java.io.Serializable

class Task(
        var title: String? = null,
        var content: String? = null,
        var cateogory: String? = null,
        var startDate: String? = null,
        var endDate: String? = null,
        var done: Boolean? = false,
        var uid: String? = null
        ) : Serializable{

}