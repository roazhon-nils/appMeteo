package com.example.sae4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import java.util.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    lateinit var bouttonPosition: ImageButton
    lateinit var bouttonRecherche: ImageButton
    lateinit var rechercheEdit: EditText
    lateinit var nomVille: TextView
    lateinit var imageMeteo : ImageView
    lateinit var textHour : TextView
    lateinit var windParHour : TextView
    lateinit var temperature : TextView
    lateinit var bouttonCarte : ImageButton
    lateinit var imageJ1 : ImageView
    lateinit var imageJ2 : ImageView
    lateinit var imageJ3: ImageView
    lateinit var temperatureMaxj1: TextView
    lateinit var temperatureMinj1: TextView
    lateinit var temperatureMaxj2: TextView
    lateinit var temperatureMinj2: TextView
    lateinit var temperatureMaxj3: TextView
    lateinit var temperatureMinj3: TextView

    lateinit var position : Position
    lateinit var request : RequestApi
    lateinit var requestDay : RequestApiJournee

    lateinit var createLogin : Button
    lateinit var loginButton : Button

    private val coroutineScope = CoroutineScope(Dispatchers.Default)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nomVille = findViewById(R.id.nomVille)
        bouttonPosition = findViewById(R.id.buttonPosition)
        imageMeteo = findViewById(R.id.imageMeteo)
        textHour = findViewById(R.id.textHour)
        imageMeteo = findViewById(R.id.imageMeteo)
        rechercheEdit = findViewById(R.id.rechercheEdit)
        bouttonRecherche = findViewById(R.id.rechercheButton)
        windParHour = findViewById(R.id.windParHour)
        temperature = findViewById(R.id.temperature)
        bouttonCarte = findViewById(R.id.bouttonCarte)
        imageJ1 = findViewById(R.id.imageJ1)
        imageJ2 = findViewById(R.id.imageJ2)
        imageJ3 = findViewById(R.id.imageJ3)
        temperatureMaxj1 = findViewById(R.id.temperatureMaxj1)
        temperatureMinj1 = findViewById(R.id.temperatureMinJ1)
        temperatureMaxj2 = findViewById(R.id.temperatureMaxJ2)
        temperatureMinj2 = findViewById(R.id.temperatureMinJ2)
        temperatureMaxj3 = findViewById(R.id.temperatureMaxJ3)
        temperatureMinj3 = findViewById(R.id.temperatureMinJ3)
        createLogin = findViewById(R.id.createLogin)
        loginButton = findViewById(R.id.loginButton)


        println("Premier print")

        position = Position(this@MainActivity,nomVille, bouttonPosition,bouttonRecherche,rechercheEdit,
        )
        position.initPosition(imageMeteo,
            temperature,
            windParHour,
            0,
            imageJ1,
            imageJ2,
            imageJ3,
            temperatureMaxj1,
            temperatureMinj1,
            temperatureMaxj2,
            temperatureMinj2,
            temperatureMaxj3,
            temperatureMinj3)
        request = RequestApi(this@MainActivity,position)
        request.request(imageMeteo,temperature,windParHour)

        requestDay = RequestApiJournee(this@MainActivity,position)
        requestDay.requestDay(imageJ1,imageJ2,imageJ3,temperatureMaxj1,temperatureMinj1,temperatureMaxj2,temperatureMinj2,temperatureMaxj3,temperatureMinj3)
        position.setRequestApi(request,requestDay)


        val barreHeure = findViewById<SeekBar>(R.id.seekBar)
        barreHeure.min = 0
        barreHeure.max = 23


        barreHeure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                println("request${request.RootHourly!!.info.hourly.weathercode[0]}")
                textHour.setText("${barreHeure.progress.toString()} h")
                //println("position $progress")
                when (request.RootHourly!!.info.hourly.weathercode[progress]) {
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
                windParHour.setText("${request.RootHourly!!.info.hourly.windspeed_10m[progress].toString()} km/h")
                temperature.setText("${request.RootHourly!!.info.hourly.temperature_2m[progress]} °C")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //Null
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //Null
            }
        })

        bouttonCarte.setOnClickListener{
            val intent1 = Intent(this,EventActivity::class.java)
            println(position.latitude.toString())
            println(position.longitude.toString())
            intent1.putExtra("latitude",position.latitude.toString())
            intent1.putExtra("longitude",position.longitude.toString())
            startActivity(intent1)
        }

        createLogin.setOnClickListener {
            val intent2 = Intent(this,CreationActivity::class.java)
            startActivity(intent2)
        }

        loginButton.setOnClickListener {
            val intent3 = Intent(this,LoginActivity::class.java)
            startActivity(intent3)
        }
    }

}