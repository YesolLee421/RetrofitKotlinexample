package com.example.retrofit_kotlin_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {



    var weatherData: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherData = findViewById(R.id.text_main)
        findViewById<View>(R.id.button_main).setOnClickListener { getCurrentData() }


    }


    companion object {
        var baseURL: String = "http://openweathermap.org/"
        var AppId: String = "13c5b01c0723247bbc4c22d24181966e"
        var lat :String = "35"
        var lon :String = "139"
    }
    internal fun getCurrentData() {

        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat,lon,AppId)
        Toast.makeText(this@MainActivity,"pre{${call.isExecuted}}버튼 클릭",Toast.LENGTH_SHORT).show()

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity,"${t.message} 실패",Toast.LENGTH_SHORT).show()
                weatherData!!.text = t.message            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.e("TAG_main","code={$response.code()}")
                //Toast.makeText(this@MainActivity,"code={$response.code()}",Toast.LENGTH_SHORT).show()
                if(response.code()==200){ //코드 성공
                    Toast.makeText(this@MainActivity,"${response.code()} 성공",Toast.LENGTH_SHORT).show()
                    val weatherResponse = response.body()!!
                    val stringBuilder = "Country:"+
                            weatherResponse.sys!!.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main!!.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main!!.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main!!.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main!!.pressure
                    Toast.makeText(this@MainActivity,stringBuilder,Toast.LENGTH_SHORT).show()
                    weatherData!!.text = stringBuilder

                }
            }

        })
        Toast.makeText(this@MainActivity,"post{${call.isExecuted}}버튼 클릭",Toast.LENGTH_SHORT).show()

    }
}
