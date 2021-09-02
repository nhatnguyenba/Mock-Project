package com.example.test.setting.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.test.R
import com.example.test.setting.dao.FirebaseDAO
import com.example.test.setting.model.Category
import kotlinx.android.synthetic.main.fragment_add_new.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCategoryFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddCategory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnAddCategory -> {
                Log.d("TAG", "Clicked submit")
                val category = Category()
                category.name_Cat = et_Name_NewCat.text.toString()
                category.des_cat = et_Description_NewCat.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    FirebaseDAO.addCategory(category)
                }
                this.activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
    }
}