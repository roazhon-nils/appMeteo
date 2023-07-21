package com.example.sae4

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Parcelable
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import kotlinx.parcelize.Parcelize
import java.io.IOException
import java.util.*


class Position(private val context: Context, nomVille : TextView, bouttonPosition : ImageButton, bouttonRecherche:ImageButton, rechercheEdit: EditText,

) {
    private var nomVille: TextView
    private var bouttonPosition: ImageButton
    private var bouttonRecherche: ImageButton
    private var rechercheEdit: EditText
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var request : RequestApi? = null
    var requestApiday: RequestApiJournee? = null

    init {
        this.nomVille = nomVille
        this.bouttonPosition = bouttonPosition
        this.bouttonRecherche = bouttonRecherche
        this.rechercheEdit = rechercheEdit
    }

    fun setRequestApi(requestApi: RequestApi, requestApiJournee: RequestApiJournee) {
        this.request = requestApi
        this.requestApiday = requestApiJournee
    }

    fun initPosition(imageMeteo : ImageView, temperature: TextView, windParHour: TextView, heure: Int,
                     imageJ1 : ImageView,
                     imageJ2: ImageView,
                     imageJ3: ImageView,
                     temperatureMaxJ1: TextView,
                     temperatureMinJ1: TextView,
                     temperatureMaxJ2: TextView,
                     temperatureMinJ2: TextView,
                     temperatureMaxJ3: TextView,
                     temperatureMinJ3: TextView) {
        var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

        //Demande la localisation
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission déjà accordée, pas besoin de demander
        } else {
            // Demander la permission d'accéder à la localisation
            requestLocationPermission()
        }

        var lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.latitude
            longitude = lastKnownLocation.longitude
            nomVille.setText(getCityName(latitude, longitude))

        } else {
            Toast.makeText(
                context,
                "La dernière position connue n'est pas disponible",
                Toast.LENGTH_SHORT
            ).show()
        }

        //actulisation avec le boutton
        bouttonPosition.setOnClickListener {
            var lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.latitude
                longitude = lastKnownLocation.longitude
                nomVille.setText(getCityName(latitude, longitude))

            } else {
                Toast.makeText(
                    context,
                    "La dernière position connue n'est pas disponible",
                    Toast.LENGTH_SHORT
                ).show()
            }
            request!!.setNewLocalisation(this.latitude, this.longitude)
            request!!.request(imageMeteo, temperature, windParHour)
            requestApiday!!.setNewLocalisation(this.latitude,this.longitude)
            requestApiday!!.requestDay(imageJ1, imageJ2, imageJ3, temperatureMaxJ1, temperatureMinJ1, temperatureMaxJ2, temperatureMinJ2, temperatureMaxJ3, temperatureMinJ3)
        }

        bouttonRecherche.setOnClickListener {
            var villeRecherche = rechercheEdit.text
            val co = getLatLngFromCityName(context,villeRecherche.toString())
            if (co != null) {
                nomVille.setText(villeRecherche)
                request!!.setNewLocalisation(this.latitude, this.longitude)
                request!!.request(imageMeteo, temperature, windParHour)
                requestApiday!!.setNewLocalisation(this.latitude,this.longitude)
                requestApiday!!.requestDay(imageJ1, imageJ2, imageJ3, temperatureMaxJ1, temperatureMinJ1, temperatureMaxJ2, temperatureMinJ2, temperatureMaxJ3, temperatureMinJ3)
            } else {
                Toast.makeText(context,"Nom de ville invalide",Toast.LENGTH_SHORT)
            }
        }
    }


    private fun requestLocationPermission() {
        // Demander la permission d'accéder à la localisation
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                context,
                "L'application a besoin d'accéder à votre localisation pour fonctionner correctement",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        var cityName = ""
        val geocoder = Geocoder(context, Locale.getDefault())
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

    fun getLatLngFromCityName(context: Context, cityName: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder.getFromLocationName(cityName, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                latitude = addresses[0].latitude
                longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}

