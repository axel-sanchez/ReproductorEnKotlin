package com.axel.reproductorenkotlin.ui.view.interfaces

import android.support.v4.app.Fragment

interface INavigationHost {

    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)

    fun replaceTo(fragment: Fragment, addToBackstack: Boolean)

    fun finish()
}