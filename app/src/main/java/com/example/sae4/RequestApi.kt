package com.example.sae4

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Serializable
data class HourlyResponse(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val hourly_units: HourlyUnits,
    val hourly: Hourly
)

@Serializable
data class Hourly(
    val time: Array<String>,
    val temperature_2m: Array<Double>,
    val weathercode: Array<Int>,
    val windspeed_10m: Array<Double>,
)

@Serializable
data class HourlyUnits(
    val time: String,
    val temperature_2m: String,
    val weathercode: String,
    val windspeed_10m: String,
)
class RequestApi(val context: Context,position: Position) {
    private var latitude : Double
    private var longitude : Double
    var start_date : String
    var end_date : String
    private var timezone : String
    private var baseUrl : String
    var hourlyResponse : HourlyResponse? = null
    var position : Position

    init {
        latitude = position.latitude
        longitude = position.longitude
        val formater = DateTimeFormat.forPattern("yyyy-MM-dd")
        start_date = formater.print(DateTime())
        end_date = formater.print(DateTime().plusDays(4))
        timezone = "Europe%2FBerlin"
        baseUrl = "https://api.open-meteo.com/v1/forecast?"
        this.position = position
    }

    fun request(imageMeteo : ImageView,temperature: TextView,windParHour: TextView){
        val queue = Volley.newRequestQueue(context)
        var url = baseUrl+"latitude=$latitude&longitude=$longitude&hourly=temperature_2m,weathercode,windspeed_10m&start_date=$start_date&end_date=$end_date"

        println(url)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Gérer la réponse de la requête ici
                hourlyResponse = Json.decodeFromString<HourlyResponse>(response)
                //Log.d("API response", hourlyResponse.toString())
                setDonnee(imageMeteo, temperature, windParHour,0)
            },
            { error ->
                // Gérer les erreurs de la requête ici
                Log.e("API Error", error.toString())
            })
        queue.add(stringRequest)
    }

    fun setDonnee(imageMeteo : ImageView,temperature: TextView,windParHour: TextView,heure: Int){
        when (hourlyResponse!!.hourly.weathercode[heure]) {
            0 -> imageMeteo.setImageResource(R.drawable.soleil)
            1, 2, 3 -> imageMeteo.setImageResource(R.drawable.soleil_nuage)
            45, 48 -> imageMeteo.setImageResource(R.drawable.brouillard)
            51, 53, 55, 56, 57 -> imageMeteo.setImageResource(R.drawable.light_rain)
            61,63,65,66,67 -> imageMeteo.setImageResource(R.drawable.moderer_rain)
            71,73,75,77 -> imageMeteo.setImageResource(R.drawable.snow)
            80,81,82 -> imageMeteo.setImageResource(R.drawable.rain)
            85,86 -> imageMeteo.setImageResource(R.drawable.snow)
            95 -> imageMeteo.setImageResource(R.drawable.orage)
            96,99 -> imageMeteo.setImageResource(R.drawable.gros_orage)
        }
        temperature.setText("${hourlyResponse!!.hourly.temperature_2m[heure]} °C")
        windParHour.setText("${hourlyResponse!!.hourly.windspeed_10m[heure].toString()} km/h")
    }

    fun setLongitude(new : Double){
        this.longitude = new
    }

    fun setlatitude(new : Double){
        this.latitude = new
    }

    fun setStartDate(new : String){
        this.start_date = new
    }

    fun setEndDate(new : String){
        this.end_date = new
    }

    fun setNewLocalisation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        println("set new localisation")
    }
}