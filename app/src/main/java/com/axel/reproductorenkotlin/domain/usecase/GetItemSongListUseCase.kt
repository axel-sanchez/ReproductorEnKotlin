package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.ItemSong
import com.axel.reproductorenkotlin.data.service.ConnectToApi

interface GetItemSongListUseCase{
    suspend fun call(): MutableList<ItemSong?>
}

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class GetItemSongListUseCaseImpl(private val api: ConnectToApi): GetItemSongListUseCase {
    override suspend fun call(): MutableList<ItemSong?> {
        return api.getItemSongList().value?: mutableListOf()
    }
}