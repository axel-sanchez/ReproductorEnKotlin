package com.axel.reproductorenkotlin.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment

class MainActivity : AppCompatActivity(), INavigationHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            var fragment: androidx.fragment.app.Fragment = MainFragment()

            replaceTo(fragment, false)
        }
    }

    override fun navigateTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment)
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun replaceTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun finish() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container) as ReproductorFragment

        if (f.childFragmentManager.backStackEntryCount > 1) {
            f.childFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}


