package com.example.test.setting.dao

import com.example.test.setting.model.Category
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDAO {
    var db : FirebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference : DatabaseReference = db.getReference(Category::class.java.simpleName)

    suspend fun addCategory(category: Category){
        databaseReference.push().setValue(category)
    }

    suspend fun deleteCategory(id : String){
        databaseReference.child(id).removeValue()
    }

    suspend fun updateCategory(pair : Pair<String?, Category>){
        databaseReference.child(pair.first.toString()).setValue(pair.second)
    }
}