package com.example.sae4


import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import java.util.*

class EventActivity : AppCompatActivity() {

    lateinit var titleEvent : TextView
    lateinit var radiusSeekBar : SeekBar
    lateinit var textRadius : TextView
    lateinit var listEvent : ListView
    lateinit var homeActivity: ImageButton
    var res = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        titleEvent = findViewById(R.id.titleEvent)
        radiusSeekBar = findViewById(R.id.radiusSeekBar)
        textRadius = findViewById(R.id.textRadius)
        listEvent = findViewById(R.id.listEvent)
        homeActivity = findViewById(R.id.homeActivity)


        homeActivity.setOnClickListener {
            finish()
        }

        fun getCityName(latitude: Double, longitude: Double): String {
            var cityName = ""
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses!!.size != 0) {
                    val address = addresses[0]
                    cityName = address.locality
                }
            } catch (e: Exception) {
                Log.e("Error", "Error getting city name: ${e.message}")
            }
            return cityName
        }


        val latitude = intent.getStringExtra("latitude")!!.toDouble()
        val longitude = intent.getStringExtra("longitude")!!.toDouble()
        var ville = getCityName(latitude,longitude)
        titleEvent.setText(ville)

        var request = RequestActivity(this, latitude,longitude,radiusSeekBar)


        request.request(1)

        radiusSeekBar.min = 1
        radiusSeekBar.max = 30


        radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                textRadius.text = "$progress km"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //Null
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                res = mutableListOf()
                request.request(radiusSeekBar.progress+1)
                var adapter  = ArrayAdapter(this@EventActivity, android.R.layout.simple_list_item_1,res)
                adapter.clear()

                if (request.activityResponse != null) {
                    for (i in 0..request.activityResponse!!.result.size - 1) {
                        //println(request.activityResponse!!.features[i].properties.name)
                        res += request.activityResponse!!.result[i].properties.name
                    }

                    listEvent.adapter = adapter

                    //println("long ${request.activityResponse!!.result[0].properties.name}")
                    //println(res.size)
                }
            }
        })


    }



}


