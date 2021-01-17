package com.axel.reproductorenkotlin.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 1337
    val REDIRECT_URI = "https://open.spotify.com/playlist/37i9dQZF1EteSOeE3vmqJb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AuthenticationRequest.Builder(
            "ad9797f1312949b59f76faaf9a709a6d",
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    Log.i("SUCCESFUL", "onActivityResult: ${response.accessToken}")
                    ConnectToApi.token = response.accessToken
                }
                AuthenticationResponse.Type.ERROR -> {
                    println("error: ${response.error}")
                }
                else -> {
                }
            }
        }
    }
}