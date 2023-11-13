package com.example.wheatherfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wheatherfinder.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWheatherdata()

    }

    private fun fetchWheatherdata() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData("jaipur", "8b11796a941c42141440706b2df5292d", "metric")
        response.enqueue(object : Callback<weatherdetails> {
            override fun onResponse(
                call: Call<weatherdetails>, response: Response<weatherdetails>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp
                }
            }

            override fun onFailure(call: Call<weatherdetails>, t: Throwable) {


            }

        })
    }
}