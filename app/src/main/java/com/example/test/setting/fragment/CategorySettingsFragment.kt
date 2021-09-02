package com.example.test.setting.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.setting.adapter.CategoryGridAdapter
import com.example.test.setting.adapter.CategoryListAdapter
import com.example.test.setting.dao.FirebaseDAO
import com.example.test.setting.model.Category
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.fragment_settings_category.*
import java.util.*
import kotlin.collections.ArrayList

class CategorySettingsFragment : Fragment(), View.OnClickListener, ChildEventListener,
    AdapterView.OnItemSelectedListener {

    companion object {
        private const val TAG = "CategorySettingsFragmen"
        private val spinnerList = arrayListOf("Default", "A to Z", "Z to A")
    }

    private var listadapter: CategoryListAdapter? = null
    private var gridAdapter: CategoryGridAdapter? = null
    private var listPairCat = ArrayList<Pair<String?, Category>>()
    private var tempListPairCat = ArrayList<Pair<String?, Category>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener(this)
        iv_listmode.setOnClickListener(this)
        iv_gridmode.setOnClickListener(this)
        spinner_sort_category.onItemSelectedListener = this
        FirebaseDAO.db.reference.child("Category").addChildEventListener(this)
        listadapter = CategoryListAdapter(
            listPairCat,
            requireActivity().supportFragmentManager,
            requireContext()
        )
        gridAdapter = CategoryGridAdapter(
            listPairCat,
            requireActivity().supportFragmentManager,
            requireContext()
        )
        setupSpinner()
        showListMode()

        Log.d(TAG, "onViewCreated: ")
    }

    private fun setupSpinner() {
        val arrayAdapter = context?.let {
            ArrayAdapter(
                it, android.R.layout.simple_spinner_dropdown_item,
                spinnerList
            )
        }
        spinner_sort_category.adapter = arrayAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab -> {
                btnFab()
            }
            R.id.iv_listmode -> {
                showListMode()
            }
            R.id.iv_gridmode -> {
                showGridMode()
            }
        }
    }

    private fun showListMode() {
        rv_list_category.layoutManager = LinearLayoutManager(context)
        rv_list_category.adapter = listadapter
        Log.d(TAG, "showListMode: ")
    }

    private fun showGridMode() {
        rv_list_category.layoutManager = GridLayoutManager(context, 2)
        rv_list_category.adapter = gridAdapter
        Log.d(TAG, "showGridMode: ")
    }

    private fun btnFab() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(NewCategoryFragment(), "newCategory")
            ?.commit()
    }


    // add listener for Firebase
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val category: Category = snapshot.getValue(Category::class.java)!!
            listPairCat.add(Pair(snapshot.key, category))
            tempListPairCat.add(Pair(snapshot.key, category))
            Log.d(TAG, "onChildAdded: ${snapshot.key}")
            listadapter?.notifyDataSetChanged()
            gridAdapter?.notifyDataSetChanged()
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val category: Category = snapshot.getValue(Category::class.java)!!
        for (i in listPairCat) {
            if (i.first.equals(snapshot.key)) {
                val index = listPairCat.indexOf(i)
                listPairCat.remove(i)
                tempListPairCat.remove(i)
                listPairCat.add(index, Pair(snapshot.key, category))
                tempListPairCat.add(index, Pair(snapshot.key, category))
                break
            }
        }
        listadapter?.notifyDataSetChanged()
        gridAdapter?.notifyDataSetChanged()
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        val category: Category = snapshot.getValue(Category::class.java)!!
        for (i in listPairCat) {
            if (i.second.name_Cat.equals(category.name_Cat) && i.second.des_cat.equals(category.des_cat) && i.first.equals(
                    snapshot.key
                )
            ) {
                listPairCat.remove(i)
                tempListPairCat.remove(i)
                break
            }
        }
        listPairCat.remove(Pair(snapshot.key, category))
        listadapter?.notifyDataSetChanged()
        gridAdapter?.notifyDataSetChanged()
        Toast.makeText(context, "Removed Category", Toast.LENGTH_SHORT).show()
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
    }

    override fun onCancelled(error: DatabaseError) {
    }

    // callback method for spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (spinnerList[position]) {
            "A to Z" -> {
                sortCategoryAZ()

            }
            "Z to A" -> {
                sortCategoryZA()
            }
            "Default" -> {
                sortDefault()
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    // sort category ArrayList
    private fun sortCategoryAZ() {
        Collections.sort(listPairCat, kotlin.Comparator { o1, o2 ->
            return@Comparator o1.second.name_Cat!!.compareTo(o2.second.name_Cat!!, true)
        })
        Log.d(TAG, "sortCategoryAZ: ${listPairCat.size}")
        listadapter?.notifyDataSetChanged()
        gridAdapter?.notifyDataSetChanged()
    }

    private fun sortCategoryZA() {
        Collections.sort(listPairCat, kotlin.Comparator { o1, o2 ->
            return@Comparator o2.second.name_Cat!!.compareTo(o1.second.name_Cat!!, true)
        })
        Log.d(TAG, "sortCategoryAZ: ${listPairCat.size}")
        listadapter?.notifyDataSetChanged()
        gridAdapter?.notifyDataSetChanged()
    }

    private fun sortDefault() {
        Log.d(TAG, "sortDefault: ")
        if (listPairCat.size != 0 && tempListPairCat.size != 0) {
            listPairCat.clear()
            for (i in tempListPairCat) {
                listPairCat.add(i)
            }
            listadapter?.notifyDataSetChanged()
            gridAdapter?.notifyDataSetChanged()
        }
    }
}