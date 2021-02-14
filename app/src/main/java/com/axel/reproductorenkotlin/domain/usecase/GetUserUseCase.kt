package com.axel.reproductorenkotlin.domain.usecase

import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.domain.repository.UserRepository

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