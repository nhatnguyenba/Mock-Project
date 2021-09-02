package com.example.test.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.test.R
import com.example.test.setting.fragment.CategorySettingsFragment
import com.example.test.setting.fragment.SettingsApplicationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_nav_setting.setOnNavigationItemSelectedListener(this)
        showCategoryFragment()
        Log.d(TAG, "onViewCreated: ")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val TAG = "SettingsFragment"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.category_manager_item -> {
                showCategoryFragment()
                return true
            }

            R.id.setting_manager_item -> {
                showSettingsAppFragment()
                return true
            }
        }
        return false
    }

    private fun showCategoryFragment() {
//        activity?.toolbar?.setTitle("Quản lý phân loại")
        activity?.toolbar?.tv_toolbar_title?.setText("Quản lý phân loại")
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.framelayout_settings, CategorySettingsFragment())?.commit()
    }

    private fun showSettingsAppFragment() {
//        activity?.toolbar?.setTitle("Thiết lập ứng dụng")
        activity?.toolbar?.tv_toolbar_title?.setText("Thiết lập ứng dụng")
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.framelayout_settings, SettingsApplicationFragment())?.commit()
    }
}