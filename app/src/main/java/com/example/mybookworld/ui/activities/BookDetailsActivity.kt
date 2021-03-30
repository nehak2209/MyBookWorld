package com.example.mybookworld.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.utils.Constants
import com.example.mybookworld.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_book_details.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class BookDetailsActivity : BaseActivity() {

    private  var mBookId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_BOOK_ID)){

            mBookId=intent.getStringExtra(Constants.EXTRA_BOOK_ID)!!
            Log.i("book id",mBookId)
        }
        getBookDetails()



    }

    private  fun getBookDetails(){

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBookDetails(this,mBookId)
    }



    fun bookDetailSuccess(book:Books){


        hideProgressDialog()
        GlideLoader(this).loadBookPicture(Uri.parse(book.imageUrl),book_detail_image)
        GlideLoader(this).loadBookPicture(Uri.parse(book.imageUrl),image_back_detail)

        tvb_book_Detail_title_label1.text=book.title
         tv_author_detail_title1.text=book.author
        book_score_detail.text=book.rating
        review_book_detail.text="Reviews:"+"${book.review}"
        book_detail_pages.text="Pages:" + "${book.pages}"
        category_detail.text=book.category
        book_detail_description.text=book.description

        //horizontal view --->PdfReaderActivity
        //vertical view--->PdfViewerActivity

        read_now.setOnClickListener {
            val intent = Intent(this, PdfReaderActivity::class.java)
                intent.putExtra("url", book.bookUrl)
                intent.putExtra("bookName", book.title)
                startActivity(intent)

        }


    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_book_detail_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24dp)
            actionBar.title = resources.getString(R.string.app_name)
        }

        toolbar_book_detail_activity.setNavigationOnClickListener { onBackPressed() }
    }
}