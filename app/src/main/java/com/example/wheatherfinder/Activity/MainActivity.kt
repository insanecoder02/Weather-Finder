package com.example.wheatherfinder.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import com.example.wheatherfinder.Interfaces.ApiInterface
import com.example.wheatherfinder.databinding.ActivityMainBinding
import com.example.wheatherfinder.DataClass.weatherdetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.hours

//
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWheatherdata("jaipur")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchCity
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWheatherdata(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWheatherdata(cityName:String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData(cityName, "8b11796a941c42141440706b2df5292d", "metric")
        response.enqueue(object : Callback<weatherdetails> {
            override fun onResponse(
                call: Call<weatherdetails>, response: Response<weatherdetails>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.dayStatus.text = condition
                    binding.condition.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.mintemp.text = "Min Temp: $minTemp °C"
                    binding.tempMax.text = "$humidity%"
                    binding.windSpeedMax.text = "$windSpeed m/s"
                    binding.sunRiseMax.text = "${time(sunRise.toLong())}"
                    binding.sunSetMax.text = "${time(sunSet.toLong())}"
                    binding.currentTemp.text = "$temperature °C"
                    binding.seaLevel.text = "$seaLevel hPa"
                    binding.currentDate.text = date()
                    binding.currentDay.text = dayName(System.currentTimeMillis())
                    binding.location.text = "$cityName"

                    changeAccToWeather(condition)

                }
            }

            override fun onFailure(call: Call<weatherdetails>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun changeAccToWeather(condition: String) {
        when(condition){
            "Smoke" ->{
                binding.root.setBackgroundColor(Color.YELLOW)
            }
        }
    }
    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}