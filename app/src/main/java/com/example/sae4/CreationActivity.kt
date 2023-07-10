package com.example.sae4

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

@Serializable
data class creation(
    val email: String,
    val password: String,
    val confirmPassword: String
)

class CreationActivity : AppCompatActivity() {

    lateinit var emailCreateLogin : EditText
    lateinit var createPassword : EditText
    lateinit var comfirmPassword : EditText
    lateinit var validationCreation : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_login)

        emailCreateLogin = findViewById(R.id.emailCreateLogin)
        createPassword = findViewById(R.id.createPassword)
        comfirmPassword = findViewById(R.id.comfirmPassword)
        validationCreation = findViewById(R.id.validationCreation)


        validationCreation.setOnClickListener {
            val newLogin = creation(emailCreateLogin.text.toString(),createPassword.text.toString(),comfirmPassword.text.toString())
            val jsonCreation = Json.encodeToString(newLogin)
            println(jsonCreation)


            val body = RequestBody.create(MediaType.parse("application/json"), jsonCreation)

            val request = Request.Builder()
                .url("http://10.0.2.2:8000/register")
                .post(body)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "Request failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(TAG, "Response received: ${response.body()?.string()}")
                    finish()
                }
        })


    }
}}