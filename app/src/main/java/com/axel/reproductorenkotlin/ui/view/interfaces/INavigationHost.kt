package com.axel.reproductorenkotlin.ui.view.interfaces

import androidx.fragment.app.Fragment

interface INavigationHost {

    fun navigateTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean)

    fun replaceTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean)

    fun finish()
}