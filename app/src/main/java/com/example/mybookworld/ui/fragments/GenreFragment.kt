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
        val autobiography = view.findViewById<CardView>(R.id.autobiography)
        val biography = view.findViewById<CardView>(R.id.biography)
        val healthFitness = view.findViewById<CardView>(R.id.health_fitness)
        val selfImprovement = view.findViewById<CardView>(R.id.self_improvement)
        horror.setOnClickListener{ changeFragment(view,"Horror")}
        romance.setOnClickListener{ changeFragment(view,"Romance")}
        fiction.setOnClickListener{ changeFragment(view,"Fiction")}
        histFict.setOnClickListener{ changeFragment(view,"Historical Fiction")}
        fantasy.setOnClickListener{ changeFragment(view,"Fantasy")}
        actionAdv.setOnClickListener{ changeFragment(view,"Action and Adventure")}
        autobiography.setOnClickListener{ changeFragment(view,"Autobiography")}
        biography.setOnClickListener{ changeFragment(view,"Biography")}
        healthFitness.setOnClickListener{ changeFragment(view,"Health and Fitness")}
        selfImprovement.setOnClickListener{ changeFragment(view,"Self-Improvement")}

    }
    private fun changeFragment(view:View,genre: String) {
        val categoryWise = CategoryWiseBooksFragment()
        val action = GenreFragmentDirections.actionGenreFragmentToCategoryWiseBooksFragment(genre)
        Navigation.findNavController(view).navigate(action)

    }


}