package com.example.mybookworld.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.adapters.MyBookListAdapters
import kotlinx.android.synthetic.main.activity_my_favourite.*
import kotlinx.android.synthetic.main.fragment_my_works.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyWorksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyWorksFragment : BaseFragment() {
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


    private  fun getUserBooksListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getUserBooksList(this)
    }

    fun successUserBookListFromFireStore(userBookList: ArrayList<Books>){
        hideProgressDialog()

        for(i in userBookList){
            Log.i("User Book",i.title)
            Log.i("Book URL",i.bookUrl)
        }
        if(userBookList.size>0){
            iv_all_user_books.visibility=View.VISIBLE
            work_not_found.visibility = View.GONE
            iv_all_user_books.layoutManager= LinearLayoutManager(activity)
            iv_all_user_books.setHasFixedSize(true)
            val adapterBooks = MyBookListAdapters(requireActivity(),userBookList)
            iv_all_user_books.adapter = adapterBooks
        }else {
            iv_all_user_books.visibility = View.GONE
            work_not_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getUserBooksListFromFireStore()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root =inflater.inflate(R.layout.fragment_my_works, container, false)
        //pdfView.
        return root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            MyWorksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}