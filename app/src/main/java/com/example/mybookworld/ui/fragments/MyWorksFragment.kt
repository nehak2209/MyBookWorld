package com.example.mybookworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.myBooks
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
class MyWorksFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_works, container, false)
    }

    private  fun getProductListFromFireStore(){

        FirestoreClass().getBooksList(this)
    }

    fun successUserBookListFromFireStore(bookList: ArrayList<myBooks>){
        if(bookList.size>0){
            iv_all_books.visibility=View.VISIBLE
            iv_all_books.layoutManager= LinearLayoutManager(activity)
            iv_all_books.setHasFixedSize(true)
            val adapterBooks= MyBookListAdapters( requireActivity(),bookList)
            iv_all_books.adapter=adapterBooks
        }

    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyWorksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyWorksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}