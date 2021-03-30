package com.example.mybookworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mybookworld.R


class GenreFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val horror = view.findViewById<CardView>(R.id.horror)
        val romance = view.findViewById<CardView>(R.id.romance)
        val fiction = view.findViewById<CardView>(R.id.fiction)
        val histFict = view.findViewById<CardView>(R.id.historical_fiction)
        val fantasy = view.findViewById<CardView>(R.id.fantasy)
        val actionAdv = view.findViewById<CardView>(R.id.action)
        horror.setOnClickListener{ changeFragment(view,"horror")}
        romance.setOnClickListener{ changeFragment(view,"romance")}
        fiction.setOnClickListener{ changeFragment(view,"fiction")}
        histFict.setOnClickListener{ changeFragment(view,"historical fiction")}
        fantasy.setOnClickListener{ changeFragment(view,"fantasy")}
        actionAdv.setOnClickListener{ changeFragment(view,"action")}
    }
    private fun changeFragment(view:View,genre: String) {
        val categoryWise = CategoryWiseBooksFragment()
        val action = GenreFragmentDirections.actionGenreFragmentToCategoryWiseBooksFragment(genre)
        Navigation.findNavController(view).navigate(action)

    }


}