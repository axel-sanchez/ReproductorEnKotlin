package com.axel.reproductorenkotlin.data.service

import com.axel.reproductorenkotlin.data.models.FeaturedPlaylist
import com.axel.reproductorenkotlin.data.models.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/api/token?grant_type=client_credentials&client_id=ad9797f1312949b59f76faaf9a709a6d&client_secret=75822014ca2943e8994978072df4086c")
    suspend fun getToken(): Response<Token>

    @GET("browse/featured-playlists")
    suspend fun getFeaturedPlaylists(@Header("Authorization") token: String, @Query("country") country: String): Response<FeaturedPlaylist>
}