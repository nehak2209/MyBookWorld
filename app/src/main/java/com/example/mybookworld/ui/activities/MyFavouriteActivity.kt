package com.example.mybookworld.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mybookworld.R
import com.example.mybookworld.utils.Constants
import kotlinx.android.synthetic.main.activity_my_favourite.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyFavouriteActivity : AppCompatActivity() {

    private var mBookId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favourite)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_BOOK_ID)){
          mBookId=intent.getStringExtra(Constants.EXTRA_BOOK_ID)!!
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

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

}