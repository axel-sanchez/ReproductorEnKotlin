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

    single<SongRepository> { SongRepositoryImpl(get() as SongRemoteSource) }
    single<SongRemoteSource> { SongRemoteSourceImpl(get(name = "service") as ApiService) }
    single<GetPlaylistSongsUseCase> { GetPlaylistSongsUseCaseImpl(get() as SongRepository) }
    single<GetSongsBySearchUseCase> { GetSongsBySearchUseCaseImpl(get() as SongRepository) }

    single<GetFeaturedPlaylistSongsUseCase> { GetFeaturedPlaylistSongsUseCaseImpl(get() as SongRepository) }

    single<UserRepository> { UserRepositoryImpl(get() as UserRemoteSource) }
    single<UserRemoteSource> { UserRemoteSourceImpl(get(name = "service") as ApiService) }
    single<GetUserUseCase> { GetUserUseCaseImpl(get() as UserRepository) }
    single<GetUserPlaylistsUseCase> { GetUserPlaylistsUseCaseImpl(get() as UserRepository) }
}