package com.example.test.calendar.timer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.test.R
import java.util.*

@SuppressLint("UseRequireInsteadOfGet")
class FragmentTimer : Fragment() {

    private lateinit var btnStart : ImageView
    private lateinit var edtHour : EditText
    private lateinit var edtMinute : EditText
    private lateinit var edtSecond : EditText
    private lateinit var edtCountDown : EditText
    private lateinit var pgrCountDown : ProgressBar
    private lateinit var tvMinute : TextView

    private var stopThread = true
    private lateinit var handler: Handler

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        btnStart = view.findViewById(R.id.btnStart)
        edtHour = view.findViewById(R.id.edtHour)
        edtMinute = view.findViewById(R.id.edtMinute)
        edtSecond = view.findViewById(R.id.edtSecond)
        edtCountDown = view.findViewById(R.id.edtCountDown)
        pgrCountDown = view.findViewById(R.id.pgrCountDown)
        tvMinute = view.findViewById(R.id.tvMinute)

        edtHour.isEnabled = false
        edtMinute.isEnabled = false
        edtSecond.isEnabled = false

        edtHour.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if(edtHour.text.toString().length < 2) {
                edtHour.setText("0${edtHour.text}")
            }
            if(edtMinute.text.toString().length < 2) {
                edtMinute.setText("0${edtMinute.text}")
            }
            if(edtSecond.text.toString().length < 2) {
                edtSecond.setText("0${edtSecond.text}")
            }
        }

        edtHour.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(edtHour.text.toString().length < 2) {
                        edtHour.setText("0${edtHour.text}")
                    }
                    if(edtMinute.text.toString().length < 2) {
                        edtMinute.setText("0${edtMinute.text}")
                    }
                    if(edtSecond.text.toString().length < 2) {
                        edtSecond.setText("0${edtSecond.text}")
                    }
                    return true
                }
                return false
            }
        })

        edtMinute.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if(edtHour.text.toString().length < 2) {
                edtHour.setText("0${edtHour.text}")
            }
            if(edtMinute.text.toString().length < 2) {
                edtMinute.setText("0${edtMinute.text}")
            }
            if(edtSecond.text.toString().length < 2) {
                edtSecond.setText("0${edtSecond.text}")
            }
        }

        edtMinute.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(edtHour.text.toString().length < 2) {
                        edtHour.setText("0${edtHour.text}")
                    }
                    if(edtMinute.text.toString().length < 2) {
                        edtMinute.setText("0${edtMinute.text}")
                    }
                    if(edtSecond.text.toString().length < 2) {
                        edtSecond.setText("0${edtSecond.text}")
                    }
                    return true
                }
                return false
            }
        })

        edtSecond.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if(edtHour.text.toString().length < 2) {
                edtHour.setText("0${edtHour.text}")
            }
            if(edtMinute.text.toString().length < 2) {
                edtMinute.setText("0${edtMinute.text}")
            }
            if(edtSecond.text.toString().length < 2) {
                edtSecond.setText("0${edtSecond.text}")
            }
        }

        edtSecond.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(edtHour.text.toString().length < 2) {
                        edtHour.setText("0${edtHour.text}")
                    }
                    if(edtMinute.text.toString().length < 2) {
                        edtMinute.setText("0${edtMinute.text}")
                    }
                    if(edtSecond.text.toString().length < 2) {
                        edtSecond.setText("0${edtSecond.text}")
                    }
                    return true
                }
                return false
            }
        })

        btnStart.setOnClickListener {
            if(edtCountDown.text.isEmpty()) {
                Toast.makeText(context, "Enter time", Toast.LENGTH_SHORT).show()
            } else {
                if(stopThread) {
                    stopThread = false
                    startAlarm(edtCountDown.text.toString().toLong()*60*1000)
                    edtCountDown.isEnabled = false
                    tvMinute.text = "Set for ${edtCountDown.text} minutes"
//                    edtHour.isEnabled = false
//                    edtMinute.isEnabled = false
//                    edtSecond.isEnabled = false
                    btnStart.setImageResource(R.drawable.ic_pause)

                    var second: Int
                    var minute = 0
                    var hour = 0

                    val progressThread = ProgressThread(edtCountDown.text.toString().toInt())
                    Thread(progressThread).start()
                    handler = @SuppressLint("HandlerLeak")
                    object : Handler() {
                        @SuppressLint("SetTextI18n")
                        override fun handleMessage(msg: Message) {
                            super.handleMessage(msg)
                            pgrCountDown.progress = msg.arg1
                            if(msg.arg1 == hour*3600 + (minute+1)*60) {
                                minute++
                                edtMinute.setText(if(minute < 10) "0$minute" else minute.toString())
                                if(minute == 60) {
                                    minute = 0
                                    edtMinute.setText(if(minute < 10) "0$minute" else minute.toString())
                                }
                            }
                            if(msg.arg1 == (hour+1)*3600) {
                                hour++
                                edtHour.setText(if(hour < 10) "0$hour" else hour.toString())

                            }

                            second = msg.arg1 - edtMinute.text.toString().toInt()*60 - edtHour.text.toString().toInt()*3600
                            edtSecond.setText(if(second < 10) "0$second" else second.toString())

                            if(msg.arg1 == edtCountDown.text.toString().toInt()*60) {
                                stopThread = true
                                cancelAlarm()
                                edtCountDown.isEnabled = true
                                tvMinute.text = "Time up"
//                                edtHour.isEnabled = true
//                                edtMinute.isEnabled = true
//                                edtSecond.isEnabled = true
                                btnStart.setImageResource(R.drawable.ic_start)
                            }
                        }
                    }
                } else {
                    stopThread = true
                    cancelAlarm()
                    edtCountDown.isEnabled = true
                    tvMinute.text = "Stopped"
//                    edtHour.isEnabled = true
//                    edtMinute.isEnabled = true
//                    edtSecond.isEnabled = true
                    btnStart.setImageResource(R.drawable.ic_start)
                }
            }
        }

        return view
    }

    inner class ProgressThread(private val minutes : Int) : Runnable {
        override fun run() {
            pgrCountDown.max = minutes*60
            for (i in 1 until minutes * 60 + 1) {
                if (stopThread) {
                    return
                }
                Thread.sleep(1000)
                val msg = handler.obtainMessage()
                msg.arg1 = i
                handler.sendMessage(msg)
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun startAlarm(time: Long) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotifyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1234, intent, 0)
        val c = Calendar.getInstance()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis + time, pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotifyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1234, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopThread = true
        cancelAlarm()
    }
}