
package com.example.mybookworld.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.adapters.MyBookListAdapters
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyWorksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    fun successProductListFromFireStore(bookList:ArrayList<Books>){
       if(bookList.size>0){
           iv_all_books.visibility=View.VISIBLE
           iv_all_books.layoutManager=LinearLayoutManager(activity)
            iv_all_books.setHasFixedSize(true)
           val adapterBooks=MyBookListAdapters( requireActivity(),bookList)
           iv_all_books.adapter=adapterBooks
       }

    }

    private  fun getProductListFromFireStore(){

         FirestoreClass().getBooksList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root =inflater.inflate(R.layout.fragment_home, container, false)
        //pdfView.
        return root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {

                }
    }
}