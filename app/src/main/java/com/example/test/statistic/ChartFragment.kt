package com.example.test.statistic

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pieChart: PieChart

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
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        //display chart
        init(view)
        setupPieChart()
        loadPieChartData()

        return view
    }

    private fun init(view: View) {
        pieChart = view.findViewById(R.id.pie_chart)
    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(false)
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        pieChart.description = null

        var legend: Legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.isWordWrapEnabled = true
        legend.setDrawInside(false)
        legend.textSize = 14f

        val pieChartMarkerView = PieChartMarkerView(requireContext(), R.layout.pie_chart_marker_view_layout)
        pieChart.marker = pieChartMarkerView
    }

    @SuppressLint("ResourceType")
    private fun loadPieChartData() {

        val sharedPreferences =
            requireContext().getSharedPreferences("statistics", Context.MODE_PRIVATE)

        //get in progress, done and overdue from shared preferences
        val inProgress = sharedPreferences.getLong("numberOfTasksIsInProgress", 0)
        val done = sharedPreferences.getLong("numberOfTasksIsDone", 0)
        val overdue = sharedPreferences.getLong("numberOfTasksIsOverdue", 0)

        var entries: MutableList<PieEntry> = mutableListOf()
        entries.add(PieEntry(inProgress.toFloat(), "In Progress"))
        entries.add(PieEntry(done.toFloat(), "Done"))
        entries.add(PieEntry(overdue.toFloat(), "Overdue"))

        var colors: MutableList<Int> = mutableListOf()
        colors.add(Color.parseColor(getString(R.color.light_green)))
        colors.add(Color.parseColor(getString(R.color.green)))
        colors.add(Color.RED)

        var pieDataSet = PieDataSet(entries, null)
        pieDataSet.colors = colors

        var pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieData.setValueTextSize(14f)
        pieData.setValueTextColor(Color.BLACK)

        pieChart.data = pieData
        pieChart.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
