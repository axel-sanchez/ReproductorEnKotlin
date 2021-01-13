package com.axel.reproductorenkotlin.ui.view

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.databinding.FragmentPlaylistBinding
import com.axel.reproductorenkotlin.ui.view.customs.ReproductorFragment
import com.axel.reproductorenkotlin.ui.view.interfaces.INavigationHost

const val ARG_ITEM = "nombre"

class NavigationFragment: ReproductorFragment() {

    private val firstItem = R.id.home
    private var itemSelected = firstItem
    lateinit var nombre: String

    override fun onBackPressFragment()= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            nombre = it.getString(ARG_ITEM)!!
        }
    }

    private var fragmentPlaylistBinding: FragmentPlaylistBinding? = null
    private val binding get() = fragmentPlaylistBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentPlaylistBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo: Por el momento no tengo un toolbar
        // (activity as AppCompatActivity).setSupportActionBar(toolbar)
        // (activity as AppCompatActivity).supportActionBar?.apply {
        //  setHomeButtonEnabled(true)
        //  setDisplayHomeAsUpEnabled(true)
        //  setDisplayShowTitleEnabled(false)}

        binding.navegador.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.reproductor.setOnClickListener {
            (activity as INavigationHost).finish()
        }

        binding.reproductor.text = nombre

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
            R.id.library -> {
                itemSelected = item.itemId
                navigateTo(LibraryFragment(), true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                itemSelected = item.itemId
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    private fun navigateTo(fragment: androidx.fragment.app.Fragment, addToBackstack: Boolean) {

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
        binding.navegador.selectedItemId = itemSelected
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String) =
            NavigationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ITEM, name)
                }
            }

    }

}