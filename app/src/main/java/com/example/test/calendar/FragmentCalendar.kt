package com.example.test.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.test.R
import com.example.test.calendar.model.DateFormat
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class FragmentCalendar : Fragment(), ICalendar {

    private lateinit var selectedTime : LocalDate
    private lateinit var tvMonthYear : TextView
    private lateinit var rvCalendar: RecyclerView
    private lateinit var btnPrevious : ImageView
    private lateinit var btnNext : ImageView

    lateinit var tvChooseDay : TextView
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager
    private lateinit var fragmentTabCalendarAdapter: FragmentTabCalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        tvMonthYear = view.findViewById(R.id.monthYearTV)
        rvCalendar = view.findViewById(R.id.rvCalendar)
        btnPrevious = view.findViewById(R.id.btnPrevious)
        btnNext = view.findViewById(R.id.btnNext)

        tvChooseDay = view.findViewById(R.id.tvChooseDay)
        tabLayout = view.findViewById(R.id.tabCalendar)
        viewPager = view.findViewById(R.id.vpTabCalendar)
        fragmentTabCalendarAdapter = FragmentTabCalendarAdapter(childFragmentManager, this)
        viewPager.adapter = fragmentTabCalendarAdapter
        tabLayout.setupWithViewPager(viewPager)

        tvChooseDay.text = dateMonthYearFromDate(LocalDate.now())
        selectedTime = LocalDate.now()
        setMonthView()

        btnPrevious.setOnClickListener {
            selectedTime = selectedTime.minusMonths(1)
            setMonthView()
        }

        btnNext.setOnClickListener {
            selectedTime = selectedTime.plusMonths(1)
            setMonthView()
        }

        return view
    }

    private fun setMonthView() {
        tvMonthYear.text = monthYearFromDate(selectedTime)
        val daysInMonth = daysInMonthArray(selectedTime)
        val adapterCalendar = AdapterCalendar(daysInMonth, this)
        rvCalendar.layoutManager = GridLayoutManager(context, 7)
        rvCalendar.adapter = adapterCalendar
    }

    private fun daysInMonthArray(date : LocalDate) : ArrayList<DateFormat> {
        val daysInMonthArray = ArrayList<DateFormat>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedTime.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for(i in 2 until 44) {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(DateFormat(null, null))
            } else {
                if(i - dayOfWeek < 10) {
                    daysInMonthArray.add(DateFormat("0" + (i-dayOfWeek), monthYearFromDate(date)))
                } else {
                    daysInMonthArray.add(DateFormat("" + (i-dayOfWeek), monthYearFromDate(date)))
                }
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date : LocalDate) : String {
        val formatter = DateTimeFormatter.ofPattern("MM/yyyy")
        return date.format(formatter)
    }

    override fun dateMonthYearFromDate(date : LocalDate) : String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return date.format(formatter)
    }

    override fun setData(text : String) {
        tvChooseDay.text = text
    }

    override fun getData() : String {
        return tvChooseDay.text.toString()
    }
}