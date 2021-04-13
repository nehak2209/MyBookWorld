package com.example.mybookworld.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.adapters.MyBookListAdapters
import com.example.mybookworld.utils.Constants
import kotlinx.android.synthetic.main.activity_my_favourite.*

class MyFavouriteActivity : BaseActivity() {

    private var mBookId: String = ""
    private  var mId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favourite)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_BOOK_ID)) {
            mBookId = intent.getStringExtra(Constants.EXTRA_BOOK_ID)!!
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_favourite_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24dp)
            actionBar.title = resources.getString(R.string.nav_my_favourite)
        }
        toolbar_my_favourite_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successFavouritesListAFromFirestore(favouritesItemList: ArrayList<Books>) {
        hideProgressDialog()
        if (favouritesItemList.size > 0) {
            rv_favourite_book_list.visibility = View.VISIBLE
            favourites_not_found.visibility = View.GONE

            rv_favourite_book_list.layoutManager = LinearLayoutManager(this)
            rv_favourite_book_list.setHasFixedSize(true)
            val adapterFavouriteBooks = MyBookListAdapters(this, favouritesItemList)
            rv_favourite_book_list.adapter = adapterFavouriteBooks

        } else {
            rv_favourite_book_list.visibility = View.GONE
            favourites_not_found.visibility = View.VISIBLE
        }
    }

       private fun getFavouritesListFromFirestore() {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getFavouritesList(this)
        }


        override fun onResume() {
            super.onResume()
            getFavouritesListFromFirestore()
        }



    }
