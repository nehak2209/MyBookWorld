package com.example.mybookworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.adapters.MyBookListAdapters
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_category_wise_books.*

class CategoryWiseBooksFragment : Fragment() {
   val args by navArgs<CategoryWiseBooksFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_wise_books, container, false)
    }

    fun successCategoryWiseListFromFireStore(bookList:ArrayList<Books>){
        if(bookList.size>0){
            cwise_all_books.visibility=View.VISIBLE
            cwise_all_books.layoutManager= LinearLayoutManager(activity)
            cwise_all_books.setHasFixedSize(true)
            val adapterBooks= MyBookListAdapters(requireActivity(),bookList)
            cwise_all_books.adapter=adapterBooks
        }

    }

    private  fun getCategoryWiseListFromFireStore(genre: String){
        FirestoreClass().getGenreBooksList(this,genre)
    }

    override fun onResume() {
        super.onResume()
        val genreName = args.genre
        setupActionBar(genreName)
        getCategoryWiseListFromFireStore(genreName)
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)
    }

    private fun setupActionBar(genreName: String) {
        (activity as AppCompatActivity).supportActionBar?.title = genreName.capitalize()
    }

}

