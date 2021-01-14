package com.axel.reproductorenkotlin.common

import android.view.View

/**
 * My extension functions
 * @author Axel Sanchez
 */

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}