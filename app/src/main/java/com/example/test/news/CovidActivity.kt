package com.example.test.news

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class CovidActivity : AppCompatActivity() {
    private var tvDateVN: TextView? = null
    private var tvConfVN: TextView? = null
    private var tvDeathVN: TextView? = null
    private var tvRecVN: TextView? = null
    private var tvActVN: TextView? = null
    private var tvDateGlob: TextView? = null
    private var tvConfGlob: TextView? = null
    private var tvDeathGlob: TextView? = null
    private var tvRecGlob: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid)
        init()
        CovidVietNam()
        CovidGlobal()
    }

    fun init() {
        tvDateVN = findViewById(R.id.dateVN)
        tvConfVN = findViewById(R.id.tvConfCov)
        tvDeathVN = findViewById(R.id.tvDeathCov)
        tvRecVN = findViewById(R.id.tvRecCov)
        tvActVN = findViewById(R.id.tvActCov)
        tvDateGlob = findViewById(R.id.dateGlob)
        tvConfGlob = findViewById(R.id.tvConfCovGlob)
        tvDeathGlob = findViewById(R.id.tvDeathCovGlob)
        tvRecGlob = findViewById(R.id.tvRecCovGlob)
    }
    fun CovidVietNam(){
        val client = OkHttpClient()
        val url1 = "https://api.covid19api.com/total/country/viet-nam"
        // Tạo request lên server.
        val request1 = Request.Builder()
                .url(url1)
                .build()

        // Thực thi request.
        client.newCall(request1).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                // Lấy thông tin JSON trả về
                val json = response.body()?.string()
                print(json)
                runOnUiThread {
                    var jsonObject: JSONArray? = null
                    try {
                        jsonObject = JSONArray(json.toString())
                        val currVN = jsonObject[jsonObject.length() - 1] as JSONObject
                        tvDateVN!!.text = currVN.getString("Date").substring(0, 10)
                        tvConfVN!!.text = currVN.getInt("Confirmed").toString()
                        tvDeathVN!!.text = currVN.getInt("Deaths").toString()
                        tvRecVN!!.text = currVN.getInt("Recovered").toString()
                        tvActVN!!.text = currVN.getInt("Active").toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error", "Network Error")
            }
        })
    }
    fun CovidGlobal(){
        val client = OkHttpClient()
        val url2 = "https://api.covid19api.com/summary"
        val request2 = Request.Builder()
                .url(url2)
                .build()

        // Thực thi request.
        client.newCall(request2).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                // Lấy thông tin JSON trả về
                val json = response.body()?.string()
                print(json)
                runOnUiThread {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(json.toString())
                        val sum = jsonObject!!.getJSONObject("Global")
                        tvDateGlob!!.text = jsonObject!!.getString("Date").substring(0, 10)
                        tvConfGlob!!.text = sum.getInt("TotalConfirmed").toString()
                        tvDeathGlob!!.text = sum.getInt("TotalDeaths").toString()
                        tvRecGlob!!.text = sum.getInt("TotalRecovered").toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error", "Network Error")
            }
        })
    }
}
