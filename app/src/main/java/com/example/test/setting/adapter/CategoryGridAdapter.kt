package com.example.test.setting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.setting.dao.FirebaseDAO
import com.example.test.setting.fragment.DetailCategoryFragment
import com.example.test.setting.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryGridAdapter(arrayList: ArrayList<Pair<String?, Category>>, fragmentManager: FragmentManager, context: Context) : RecyclerView.Adapter<CategoryGridAdapter.CategoryViewHolder>() {
    private var mList: ArrayList<Pair<String?, Category>> = arrayList
    private var mFragmentManager : FragmentManager = fragmentManager
    private var mContext = context

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_category_name : TextView = itemView.findViewById(R.id.tv_category_name)
    }

                                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_grid_item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.tv_category_name.text = mList[position].second.name_Cat
        holder.itemView.setOnClickListener {
            DetailCategoryFragment(mList[position]).show(mFragmentManager, "Detail Frag")
        }
        holder.itemView.setOnLongClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                FirebaseDAO.deleteCategory(mList[position].first.toString())
                Toast.makeText(mContext, "Deleted Category ${mList[position].second.name_Cat.toString()}", Toast.LENGTH_SHORT).show()

            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}