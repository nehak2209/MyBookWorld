package com.example.mybookworld.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.adapters.MyBookListAdapters
import kotlinx.android.synthetic.main.fragment_category_wise_books.*
import kotlinx.android.synthetic.main.fragment_home.*

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
            val adapterBooks= MyBookListAdapters( requireActivity(),bookList)
            cwise_all_books.adapter=adapterBooks
        }

    }

    private  fun getCategoryWiseListFromFireStore(genre: String){
        FirestoreClass().getGenreBooksList(this,genre)
    }

    override fun onResume() {
        super.onResume()
        val genreName = args.genre
        getCategoryWiseListFromFireStore(genreName)
    }

}