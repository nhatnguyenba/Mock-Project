package com.example.test.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.todolist.Task

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FiltersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FiltersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var filterMessage: TextView
    private lateinit var taskForStatisticAdatper: TaskForStatisticAdapter
    private lateinit var statsticDao: StatisticDao
    private var tasks = mutableListOf<Task>()

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
        val view = inflater.inflate(R.layout.fragment_filters, container, false)

        //init
        recyclerView = view.findViewById(R.id.filters_recycler_view)
        spinner = view.findViewById(R.id.filters_spinner)
        filterMessage = view.findViewById(R.id.filter_message_txt)
        statsticDao = StatisticDao.getInstance(requireContext())
        tasks.addAll(statsticDao.getTasksIsToday())
        taskForStatisticAdatper = TaskForStatisticAdapter(requireContext(), tasks)
        recyclerView.adapter = taskForStatisticAdatper
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //listen to selected item of spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        tasks.clear()
                        tasks.addAll(statsticDao.getTasksIsToday())
                        taskForStatisticAdatper.notifyDataSetChanged()
                        showEmptyMessage()
                    }
                    1 -> {
                        tasks.clear()
                        tasks.addAll(statsticDao.getTasksIsOverdue())
                        taskForStatisticAdatper.notifyDataSetChanged()
                        showEmptyMessage()
                    }
                    2 -> {
                        tasks.clear()
                        tasks.addAll(statsticDao.getTasksIsInTime())
                        taskForStatisticAdatper.notifyDataSetChanged()
                        showEmptyMessage()
                    }
                    3 -> {
                        tasks.clear()
                        tasks.addAll(statsticDao.getTasksIsCompleted())
                        taskForStatisticAdatper.notifyDataSetChanged()
                        showEmptyMessage()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        return view
    }

    private fun showEmptyMessage() {
        if (tasks.size > 0) {
            filterMessage.visibility = View.GONE
        } else {
            filterMessage.visibility = View.VISIBLE
            filterMessage.text = "There are no tasks here."
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FiltersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FiltersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
