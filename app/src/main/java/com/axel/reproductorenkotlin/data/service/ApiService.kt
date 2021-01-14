package com.axel.reproductorenkotlin.data.service

import com.axel.reproductorenkotlin.data.models.FeaturedPlaylist
import com.axel.reproductorenkotlin.data.models.Token
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/api/token?grant_type=client_credentials&client_id=ad9797f1312949b59f76faaf9a709a6d&client_secret=75822014ca2943e8994978072df4086c")
    fun getToken(): Call<Token>

    @GET("browse/featured-playlists")
    fun getFeaturedPlaylists(@Header("Authorization") token: String): Call<FeaturedPlaylist>
}