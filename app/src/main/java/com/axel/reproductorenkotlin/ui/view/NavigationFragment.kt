package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost
import kotlinx.android.synthetic.main.fragment_navigation.*

const val ARG_ITEM = "nombre"

class NavigationFragment: ReproductorFragment() {

    private val firstItem = R.id.home
    private var itemSelected = firstItem
    lateinit var nombre: String

    override fun OnBackPressFragment()= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            nombre = it.getString(ARG_ITEM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo: Por el momento no tengo un toolbar
        // (activity as AppCompatActivity).setSupportActionBar(toolbar)
        // (activity as AppCompatActivity).supportActionBar?.apply {
        //  setHomeButtonEnabled(true)
        //  setDisplayHomeAsUpEnabled(true)
        //  setDisplayShowTitleEnabled(false)}

        navegador.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        reproductor.setOnClickListener {
            (activity as INavigationHost).finish()
        }

        reproductor.text = nombre

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                navigateTo(HomeFragment(), true)
                itemSelected = item.itemId

                return@OnNavigationItemSelectedListener true
            }
            R.id.explore -> {
                navigateTo(ExploreFragment(), true)
                itemSelected = item.itemId
                return@OnNavigationItemSelectedListener true
            }
            R.id.buscar -> {
                //todo: todavía no está terminado el fragment de Buscar
                //navigateTo(ProfileFragment(), true)
                itemSelected = item.itemId
                return@OnNavigationItemSelectedListener true
            }
            R.id.biblioteca -> {
                itemSelected = item.itemId
                navigateTo(BibliotecaFragment(), true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                itemSelected = item.itemId
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    fun navigateTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean) {

        val transaction = childFragmentManager
            .beginTransaction()
            .replace(R.id.contenedor, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        navegador.selectedItemId = itemSelected
    }

    companion object {
        @JvmStatic
        fun newInstance(nombre: String) =
            NavigationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ITEM, nombre)
                }
            }

    }

}