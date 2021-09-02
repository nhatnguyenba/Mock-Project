package com.example.test.news

import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.test.R
import okhttp3.*
import org.json.JSONObject
import org.json.JSONException
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    private var spnWea: Spinner? = null
    private var btnWea: Button? = null
    private var img: ImageView? = null
    private var tvTemp: TextView? = null
    private var tvAQI: TextView? = null
    private var tvNote: TextView? = null
    private var tvHumid: TextView? = null
    private var tvWind: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        init()
        btnWea?.setOnClickListener {
            val city = spnWea!!.selectedItem.toString()
            val client = OkHttpClient()
            val url = "https://api.airvisual.com/v2/city?city=$city&state=Hanoi&country=Vietnam&key=2f081656-6713-4701-b682-749ae1df0105"
            // Tạo request lên server.
            val request = Request.Builder()
                .url(url)
                .build()

            // Thực thi request.
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Error", "Network Error")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

                    // Lấy thông tin JSON trả về
                    val json = response.body()!!.string()
                    runOnUiThread {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(json)
                            val weather = jsonObject.getJSONObject("data").getJSONObject("current")
                                .getJSONObject("weather")
                            val pollution =
                                jsonObject.getJSONObject("data").getJSONObject("current")
                                    .getJSONObject("pollution")
                            val imgName = weather.getString("ic")
                            println("====================$imgName")
                            when (imgName) {
                                "01d" -> img!!.setImageResource(R.drawable.w01d)
                                "01n" -> img!!.setImageResource(R.drawable.w01n)
                                "02d" -> img!!.setImageResource(R.drawable.w02d)
                                "02n" -> img!!.setImageResource(R.drawable.w02n)
                                "03d" -> img!!.setImageResource(R.drawable.w03d)
                                "04d" -> img!!.setImageResource(R.drawable.w04d)
                                "04n" -> img!!.setImageResource(R.drawable.w04d)
                                "09d" -> img!!.setImageResource(R.drawable.w09d)
                                "10d" -> img!!.setImageResource(R.drawable.w10d)
                                "10n" -> img!!.setImageResource(R.drawable.w10n)
                                "11d" -> img!!.setImageResource(R.drawable.w11d)
                                "13d" -> img!!.setImageResource(R.drawable.w13d)
                                "50d" -> img!!.setImageResource(R.drawable.w50d)
                                else -> img!!.setImageResource(R.drawable.w01d)
                            }
                            tvTemp!!.text = weather.getInt("tp").toString()
                            tvHumid!!.text = weather.getDouble("hu").toString()
                            tvWind!!.text = weather.getDouble("ws").toString()
                            val aqi = pollution.getDouble("aqius")
                            tvAQI!!.text = aqi.toString()
                            if (aqi > 301) {
                                tvNote!!.text = "Nguy hiểm"
                            } else if (aqi <= 300 && aqi >= 201) {
                                tvNote!!.text = "Rất ô nhiễm"
                            } else if (aqi <= 200 && aqi >= 151) {
                                tvNote!!.text = "Ô nhiễm"
                            } else if (aqi <= 150 && aqi >= 101) {
                                tvNote!!.text = "Không tốt cho người thuộc nhóm nhạy cảm"
                            } else if (aqi <= 100 && aqi >= 51) {
                                tvNote!!.text = "Vừa phải"
                            } else {
                                tvNote!!.text = "Tốt"
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            })
        }
    }

    fun init() {
        spnWea = findViewById(R.id.spnCityWea)
        btnWea = findViewById(R.id.btnWea)
        img = findViewById(R.id.imgWeather)
        tvTemp = findViewById(R.id.tvTempWea)
        tvAQI = findViewById(R.id.tvAQIWea)
        tvNote = findViewById(R.id.tvNoteWea)
        tvHumid = findViewById(R.id.tvHumidWea)
        tvWind = findViewById(R.id.tvWindWea)
    }
}