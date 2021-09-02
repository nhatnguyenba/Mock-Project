package com.example.test.news

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.test.R


class NewsFragment : Fragment() {
    private var imgWeather: ImageView? = null
    private var imgCovid: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)
        init(rootView)

        imgWeather!!.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        imgCovid!!.setOnClickListener {
            val intent = Intent(activity, CovidActivity::class.java)
            intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        return rootView
    }

    fun init(view: View) {
        imgWeather = view.findViewById(R.id.imgWeather)
        imgCovid = view.findViewById(R.id.imgCovid)
    }

}
