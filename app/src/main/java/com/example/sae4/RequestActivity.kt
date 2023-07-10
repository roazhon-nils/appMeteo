package com.example.sae4

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Math.cos
import java.lang.Math.sqrt
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.sin

@Serializable
data class ActivityResponse (
    val result: Array<Result>
    )

@Serializable
data class Result (
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val properties: Properties
)



@Serializable
data class Properties (
    val preview: String,
    val xid: String,
    val rate: Double,
    val name: String,
    val kinds: String,
    val wikipedia: String,
    val wikidata: String,
    val url: String? = null,
    val stars: Int? = null,
    val lat_max: Double? = null,
    val lat_min: Double? = null,
    val lon_max: Double? = null,
    val lon_min: Double? = null
)

data class Coordinates(val latitude: Double, val longitude: Double)

class RequestActivity(val context: Context,latitudeCentre : Double, longitudeCentre: Double, radiusSeekBar: SeekBar) {
    private var baseUrl : String
    var latitudeCentre : Double
    var longitudeCentre : Double
    val apiKey : String
    var activityResponse : ActivityResponse? = null
    var radiusSeekBar : SeekBar
    init {
        this.baseUrl = "http://10.0.2.2:8000/features/inRange?"
        this.apiKey = "5ae2e3f221c38a28845f05b6e046a8b66c60cb7f71565bbfba663fe0"
        this.latitudeCentre = latitudeCentre
        this.longitudeCentre = longitudeCentre
        this.radiusSeekBar = radiusSeekBar
    }

    fun request(dist : Int){
        val queue = Volley.newRequestQueue(context)

        val url = baseUrl+"longitude=$longitudeCentre&latitude=$latitudeCentre&range=${dist*1000}"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Gérer la réponse de la requête ici
                println(url)
                activityResponse = Json.decodeFromString<ActivityResponse>(response)
                Log.d("API response activiter", activityResponse.toString())
            },
            { error ->
                // Gérer les erreurs de la requête ici
                Log.e("API Error", error.toString())
            })
        queue.add(stringRequest)
        queue.start()
    }


}