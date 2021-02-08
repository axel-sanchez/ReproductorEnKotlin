package com.axel.reproductorenkotlin.di

import com.axel.reproductorenkotlin.data.service.ApiService
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import com.axel.reproductorenkotlin.domain.usecase.*
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.spotify.com/v1/"
/**
 * @author Axel Sanchez
 */
val moduleApp = module {
    single { ConnectToApi() }

    single(name = "retrofitToken") {
        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single(name = "serviceToken") { (get(name = "retrofitToken") as Retrofit).create(ApiService::class.java) }

    single(name = "retrofit") {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single(name = "service") { (get(name = "retrofit") as Retrofit).create(ApiService::class.java) }

    single { GetPlaylistSongsUseCaseImpl() as GetPlaylistSongsUseCase }
    single { GetSongsBySearchUseCaseImpl() as GetSongsBySearchUseCase }

    single { GetUserUseCaseImpl() as GetUserUseCase }

    single { GetUserPlaylistsUseCaseImpl() as GetUserPlaylistsUseCase }

    single { GetItemSongListUseCaseImpl() as GetItemSongListUseCase }
}