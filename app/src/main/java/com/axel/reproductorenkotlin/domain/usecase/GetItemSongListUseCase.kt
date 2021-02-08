package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetItemSongListUseCase{
    suspend fun call(): MutableList<ItemSong?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetItemSongListUseCaseImpl : KoinComponent, GetItemSongListUseCase {
    private val api: ConnectToApi by inject()

    override suspend fun call(): MutableList<ItemSong?> {
        return api.getItemSongList().value?: mutableListOf()
    }
}