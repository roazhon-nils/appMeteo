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
data class RootDaily (
    val info : ForecastResponse
)

@Serializable
data class DailyUnits(
    val time: String,
    val weathercode: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String
)

@Serializable
data class Daily(
    val time: List<String>,
    val weathercode: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>
)

@Serializable
data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    //val current_weather: CurrentWeather,
    val daily_units: DailyUnits,
    val daily: Daily
)

class RequestApiJournee(val context: Context, position: Position) {
    private var latitude : Double
    private var longitude : Double
    var start_date : String
    var end_date : String
    private var timezone : String
    private var baseUrl : String
    var dayResponse : RootDaily? = null
    var position : Position

    init {
        latitude = position.latitude
        longitude = position.longitude
        val formater = DateTimeFormat.forPattern("yyyy-MM-dd")
        start_date = formater.print(DateTime().plusDays(1))
        end_date = formater.print(DateTime().plusDays(4))
        timezone = "Europe%2FBerlin"
        baseUrl = "http://10.0.2.2:8000/weather/daily?"
        this.position = position
    }
    fun requestDay(imageJ1 : ImageView,
                   imageJ2: ImageView,
                   imageJ3: ImageView,
                   temperatureMaxJ1: TextView,
                   temperatureMinJ1: TextView,
                   temperatureMaxJ2: TextView,
                   temperatureMinJ2: TextView,
                   temperatureMaxJ3: TextView,
                   temperatureMinJ3: TextView
    ){
        val queueJournee = Volley.newRequestQueue(context)
        var url = baseUrl+"latitude=$latitude&longitude=$longitude&start_date=$start_date&end_date=$end_date"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Gérer la réponse de la requête ici

                dayResponse = Json.decodeFromString<RootDaily>(response)
                Log.d("API response", dayResponse.toString())

                setDonne(imageJ1,imageJ2,imageJ3,temperatureMaxJ1,temperatureMinJ1,temperatureMaxJ2,temperatureMinJ2,temperatureMaxJ3,temperatureMinJ3)
            },
            { error ->
                // Gérer les erreurs de la requête ici
                Log.e("API Error", error.toString())
            })

        queueJournee.add(stringRequest)

    }

    fun setDonne(imageJ1: ImageView,
                 imageJ2: ImageView,
                 imageJ3: ImageView,
                 temperatureMaxJ1: TextView,
                 temperatureMinJ1: TextView,
                 temperatureMaxJ2: TextView,
                 temperatureMinJ2: TextView,
                 temperatureMaxJ3: TextView,
                 temperatureMinJ3: TextView
    ){
        var image = arrayOf(imageJ1,imageJ2,imageJ3)
        for (i in 0..2){
            when (dayResponse!!.info.daily.weathercode[i]) {
                0 -> image[i].setImageResource(R.drawable.soleil)
                1, 2, 3 -> image[i].setImageResource(R.drawable.soleil_nuage)
                45, 48 -> image[i].setImageResource(R.drawable.brouillard)
                51, 53, 55, 56, 57 -> image[i].setImageResource(R.drawable.light_rain)
                61,63,65,66,67 -> image[i].setImageResource(R.drawable.moderer_rain)
                71,73,75,77 -> image[i].setImageResource(R.drawable.snow)
                80,81,82 -> image[i].setImageResource(R.drawable.rain)
                85,86 -> image[i].setImageResource(R.drawable.snow)
                95 -> image[i].setImageResource(R.drawable.orage)
                96,99 -> image[i].setImageResource(R.drawable.gros_orage)
            }
        }
        var temperatureMax = arrayOf(temperatureMaxJ1,temperatureMaxJ2,temperatureMaxJ3)
        var temperatureMin = arrayOf(temperatureMinJ1,temperatureMinJ2,temperatureMinJ3)
        for (i in 0..2){
            temperatureMax[i].setText("${dayResponse!!.info.daily.temperature_2m_max[i].toString()} °C")
        }
        for (i in 0..2){
            temperatureMin[i].setText("${dayResponse!!.info.daily.temperature_2m_min[i].toString()} °C")
        }
    }

    fun setNewLocalisation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        println("set new localisation")
    }
}