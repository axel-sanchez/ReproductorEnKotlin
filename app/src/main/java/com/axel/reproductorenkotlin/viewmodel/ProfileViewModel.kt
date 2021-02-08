package com.axel.reproductorenkotlin.viewmodel

import androidx.lifecycle.*
import com.axel.reproductorenkotlin.data.models.User
import com.axel.reproductorenkotlin.domain.usecase.ProfileUseCase
import kotlinx.coroutines.launch

/**
 * View model de [ProfileFragment]
 * @author Axel Sanchez
 */
class ProfileViewModel(private val profileUseCase: ProfileUseCase) : ViewModel() {

    private val listData: MutableLiveData<User?> by lazy {
        MutableLiveData<User?>().also{
            getUser()
        }
    }

    private fun setListData(user: User?) {
        listData.postValue(user)
    }

    private fun getUser() {
        viewModelScope.launch {
            setListData(profileUseCase.getUser())
        }
    }

    fun getUserLiveData(): LiveData<User?> {
        return listData
    }

    class ProfileViewModelFactory(private val profileUseCase: ProfileUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(ProfileUseCase::class.java).newInstance(profileUseCase)
        }
    }
}