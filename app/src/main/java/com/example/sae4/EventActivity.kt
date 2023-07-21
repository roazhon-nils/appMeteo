package com.example.sae4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi

class EventActivity : AppCompatActivity() {

    lateinit var titleEvent : TextView
    lateinit var radiusSeekBar : SeekBar
    lateinit var textRadius : TextView
    lateinit var listEvent : ListView
    var res = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        titleEvent = findViewById(R.id.titleEvent)
        radiusSeekBar = findViewById(R.id.radiusSeekBar)
        textRadius = findViewById(R.id.textRadius)
        listEvent = findViewById(R.id.listEvent)



        val latitude = intent.getStringExtra("latitude")!!.toDouble()
        val longitude = intent.getStringExtra("longitude")!!.toDouble()
        titleEvent.setText("$latitude $longitude")

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
                request.request(radiusSeekBar.progress)
                var adapter  = ArrayAdapter(this@EventActivity, android.R.layout.simple_list_item_1,res)
                adapter.clear()
                for (i in 0 .. request.activityResponse!!.features.size-1){
                    //println(request.activityResponse!!.features[i].properties.name)
                    res += request.activityResponse!!.features[i].properties.name
                }

                listEvent.adapter = adapter

                println("long ${request.activityResponse!!.features.size}")
                println(res.size)
            }
        })
    }

}


