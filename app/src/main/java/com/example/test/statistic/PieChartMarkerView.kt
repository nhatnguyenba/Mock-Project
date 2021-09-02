package com.example.test.statistic


import android.content.Context
import android.widget.TextView
import com.example.test.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class PieChartMarkerView : MarkerView {
    private var tvContent: TextView? = null
    private var tvNumber: TextView? = null

    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource) {
        // this marker view only displays a textview
        tvContent = findViewById(R.id.tvContent)
        tvNumber = findViewById(R.id.tvNumber)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val pieEntry = e as PieEntry
        tvContent?.text = pieEntry.label
        tvNumber?.text = pieEntry.value.toInt().toString().plus(" tasks")
        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }
}
