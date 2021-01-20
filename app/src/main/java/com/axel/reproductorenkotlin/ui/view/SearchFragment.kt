package com.axel.reproductorenkotlin.ui.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.axel.reproductorenkotlin.databinding.FragmentSearchBinding
import com.axel.reproductorenkotlin.helpers.NetworkHelper.isOnline

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private var fragmentSearchBinding: FragmentSearchBinding? = null
    private val binding get() = fragmentSearchBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (binding.search.text.isNullOrEmpty()) Toast.makeText(context, "You must enter your search", Toast.LENGTH_SHORT).show()
            else{
                if (id == EditorInfo.IME_ACTION_SEARCH || id == EditorInfo.IME_NULL) {

                    if (isOnline(requireContext())) {
                        val action = SearchFragmentDirections.toSongsFragment(query = binding.search.text.toString(), isSearch = true)
                        findNavController().navigate(action)
                    } else {
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.search.windowToken, 0)
                        Toast.makeText(requireContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show()
                    }

                    return@OnEditorActionListener true
                }
            }
            false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSearchBinding = null
    }
}