package com.example.test.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.test.R
import com.example.test.setting.dao.FirebaseDAO
import com.example.test.setting.model.Category
import kotlinx.android.synthetic.main.fragment_detail_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailCategoryFragment(category : Pair<String?, Category>) : DialogFragment(), View.OnClickListener {
    private val mCategory = category

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_save_category.setOnClickListener(this)

        tv_detail_category_id.text = mCategory.first
        et_Name_DetailCat.setText(mCategory.second.name_Cat)
        et_Des_DetailCat.setText(mCategory.second.des_cat)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.btn_save_category -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val cat = Category()
                    cat.name_Cat = et_Name_DetailCat.text.toString()
                    cat.des_cat = et_Des_DetailCat.text.toString()
                    FirebaseDAO.updateCategory(Pair(mCategory.first, cat))
                    Toast.makeText(context, "Saved Category", Toast.LENGTH_SHORT).show()
                }
                this.activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
    }

}