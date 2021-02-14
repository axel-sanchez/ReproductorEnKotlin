package com.axel.reproductorenkotlin.di

import com.axel.reproductorenkotlin.data.repository.SongRepositoryImpl
import com.axel.reproductorenkotlin.data.repository.UserRepositoryImpl
import com.axel.reproductorenkotlin.data.service.ApiService
import com.axel.reproductorenkotlin.data.source.SongRemoteSource
import com.axel.reproductorenkotlin.data.source.SongRemoteSourceImpl
import com.axel.reproductorenkotlin.data.source.UserRemoteSource
import com.axel.reproductorenkotlin.data.source.UserRemoteSourceImpl
import com.axel.reproductorenkotlin.domain.repository.SongRepository
import com.axel.reproductorenkotlin.domain.repository.UserRepository
import com.axel.reproductorenkotlin.domain.usecase.*
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.spotify.com/v1/"
/**
 * @author Axel Sanchez
 */
val moduleApp = module {
    single(name = "retrofit") {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single(name = "service") { (get(name = "retrofit") as Retrofit).create(ApiService::class.java) }

    single { SongRepositoryImpl(get() as SongRemoteSource) as SongRepository }
    single { SongRemoteSourceImpl(get(name = "service") as ApiService) as SongRemoteSource }
    single { GetPlaylistSongsUseCaseImpl(get() as SongRepository) as GetPlaylistSongsUseCase }
    single { GetSongsBySearchUseCaseImpl(get() as SongRepository) as GetSongsBySearchUseCase }

    single { GetFeaturedPlaylistSongsUseCaseImpl(get() as SongRepository) as GetFeaturedPlaylistSongsUseCase }

    single { UserRepositoryImpl(get() as UserRemoteSource) as UserRepository }
    single { UserRemoteSourceImpl(get(name = "service") as ApiService) as UserRemoteSource }
    single { GetUserUseCaseImpl(get() as UserRepository) as GetUserUseCase }
    single { GetUserPlaylistsUseCaseImpl(get() as UserRepository) as GetUserPlaylistsUseCase }
}