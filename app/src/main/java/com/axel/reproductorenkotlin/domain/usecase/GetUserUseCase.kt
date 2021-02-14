package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import com.axel.reproductorenkotlin.domain.repository.UserRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface GetUserUseCase{
    suspend fun call(): User?
}

/**
 * Use case for ProfileViewModel
 * @author Axel Sanchez
 */
class GetUserUseCaseImpl(private val repository: UserRepository): GetUserUseCase {
    override suspend fun call(): User? {
        return repository.getUser().value
    }
}