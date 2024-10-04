package com.axel.reproductorenkotlin.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.axel.reproductorenkotlin.R
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

const val REQUEST_CODE = 1337
const val REDIRECT_URI = "https://open.spotify.com/playlist/37i9dQZF1EteSOeE3vmqJb"

/**
 * @author Axel Sanchez
 */
class AuthenticationActivity: AppCompatActivity() {

    private val builder = AuthenticationRequest.Builder(
        "ad9797f1312949b59f76faaf9a709a6d",
        AuthenticationResponse.Type.TOKEN,
        REDIRECT_URI
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        findViewById<Button>(R.id.btnAuthentication).setOnClickListener {
            authentication()
        }
    }

    private fun authentication(){
        builder.setScopes(arrayOf("streaming", "playlist-read-private"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response?.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    Log.i("SUCCESSFUL", "onActivityResult: ${response.accessToken}")
                    token = response.accessToken?:""
                    goToMainActivity()
                }
                AuthenticationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
                    Log.e("Error Auth", response.error?:"")
                }
                else -> {
                    Log.e("No valid response", response?.error?:"")
                }
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        var token = ""
    }
}