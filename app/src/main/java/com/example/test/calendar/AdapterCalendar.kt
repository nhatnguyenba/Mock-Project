package com.example.test.calendar

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.calendar.model.DateFormat
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
class AdapterCalendar(private val daysOfMonth : ArrayList<DateFormat>, context: FragmentCalendar) : RecyclerView.Adapter<AdapterCalendar.ItemViewHolder>() {

    private var iCalendar: ICalendar = context

    private var isOnClick = false
    private var _position : Int = -1

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val dayOfMonth : TextView = itemView.findViewById(R.id.tvCellDay)
        val cellDay : ConstraintLayout = itemView.findViewById(R.id.cellDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_calendar, parent, false)
        val layoutParams = itemView.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dateFormat = daysOfMonth[position]
        holder.dayOfMonth.text = dateFormat.date
        val today = iCalendar.dateMonthYearFromDate(LocalDate.now())
        if(dateFormat.toString() == today || dateFormat.toString() == iCalendar.getData()) {
            holder.cellDay.setBackgroundColor(R.color.purple_200)
        }

        holder.itemView.setOnClickListener {
            if(dateFormat.toString() == "null/null") {
                // do nothing
            } else {
                iCalendar.setData(dateFormat.toString())
                isOnClick = true
                _position = holder.position
                notifyDataSetChanged()
                onBindViewHolder(holder, _position)
            }
        }

        if(isOnClick) {
            Log.d(TAG, "onBindViewHolder: $_position - $position")
            if(_position == position) {
                holder.cellDay.setBackgroundColor(R.color.black)
            } else {
                holder.dayOfMonth.setBackgroundColor(Color.TRANSPARENT)
            }
        }

    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }
}