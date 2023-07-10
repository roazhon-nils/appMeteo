package com.example.sae4

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    @Serializable
    data class creation(
        val email: String,
        val password: String,
    )

    @Serializable
    data class LoginResponse(
        val token : Token
    )

    @Serializable
    data class Token(
        val token : String,
        val permission : String
    )

    lateinit var loginEmail : EditText
    lateinit var loginPassword: EditText
    lateinit var loginValidate : ImageButton
    var loginResponse : LoginResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        loginEmail = findViewById(R.id.loginEmail)
        loginPassword = findViewById(R.id.loginPassword)
        loginValidate = findViewById(R.id.loginValidate)

        loginValidate.setOnClickListener {
            val Login = creation(loginEmail.text.toString(),loginPassword.text.toString())
            val jsonCreation = Json.encodeToString(Login)
            println(jsonCreation)

            val body = RequestBody.create(MediaType.parse("application/json"), jsonCreation)

            val request = Request.Builder()
                .url("http://10.0.2.2:8000/login")
                .post(body)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(ContentValues.TAG, "Request failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(ContentValues.TAG, "Response received: ${response.body()?.string()}")
                    loginResponse = Json.decodeFromString<LoginResponse>(response.body()!!.string())
                    finish()
                }
        })


    }

}}