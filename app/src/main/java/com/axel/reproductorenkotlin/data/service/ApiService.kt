package com.axel.reproductorenkotlin.data.service

import com.axel.reproductorenkotlin.data.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("browse/featured-playlists")
    suspend fun getFeaturedPlaylists(@Header("Authorization") token: String, @Query("country") country: String): Response<FeaturedPlaylist?>

    @GET("me")
    suspend fun getUser(@Header("Authorization") token: String): Response<User?>

    @GET("me/playlists")
    suspend fun getUserPlaylists(@Header("Authorization") token: String): Response<UserPlaylists?>

    @GET("playlists/{playlist_id}/tracks")
    suspend fun getSongsFromPlaylist(@Header("Authorization") token: String, @Path("playlist_id") idPlaylist: String, @Query("market") market: String): Response<PlaylistSongs?>

    @GET("search")
    suspend fun getSongsBySearch(@Header("Authorization") token: String, @Query("q") query: String, @Query("type") type: String = "track"): Response<Search?>
}