package com.axel.reproductorenkotlin.domain

import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class ExploreUseCase : KoinComponent {
    private val api: ConnectToApi by inject()

    suspend fun getItemSongList(): MutableList<ItemSong?> {
        return api.getItemSongList().value?: mutableListOf()
    }
}