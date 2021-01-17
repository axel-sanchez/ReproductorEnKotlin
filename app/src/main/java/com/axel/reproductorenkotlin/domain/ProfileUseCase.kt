package com.axel.reproductorenkotlin.domain

import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Use case for ProfileViewModel
 * @author Axel Sanchez
 */
class ProfileUseCase : KoinComponent {
    private val api: ConnectToApi by inject()

    suspend fun getUser(): User? {
        return api.getUser().value
    }
}