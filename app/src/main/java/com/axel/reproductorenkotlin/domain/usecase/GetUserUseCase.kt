package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetUserUseCase{
    suspend fun call(): User?
}

/**
 * Use case for ProfileViewModel
 * @author Axel Sanchez
 */
class GetUserUseCaseImpl : KoinComponent, GetUserUseCase {
    private val api: ConnectToApi by inject()

    override suspend fun call(): User? {
        return api.getUser().value
    }
}