package com.example.mybookworld.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.utils.Constants
import com.example.mybookworld.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_book_details.*

class BookDetailsActivity : BaseActivity(), View.OnClickListener {

    private  var mBookId:String=""
    private lateinit var mBookDetails: Books

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_BOOK_ID)){

            mBookId=intent.getStringExtra(Constants.EXTRA_BOOK_ID)!!
            Log.i("book id",mBookId)
        }
        favourites.setOnClickListener(this)
        favouritesRed.setOnClickListener(this)
        getBookDetails()

    }

    private  fun getBookDetails(){

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBookDetails(this,mBookId)
    }

    fun bookExistsInFavourites(){
        hideProgressDialog()
        favourites.visibility=View.GONE
        favouritesRed.visibility=View.VISIBLE
    }



    fun bookDetailSuccess(book:Books){
        mBookDetails = book


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


        FirestoreClass().checkIfItemExistInFavourites(this,mBookId)
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

    /*private fun addToFavourites(){
        val addtoFavourites= favouritesItem(
            FirestoreClass().getCurrentUserID(),
            mBookId,
            mBookDetails.title,
            mBookDetails.author,
            mBookDetails.category,
            mBookDetails.pages,
            mBookDetails.rating,
            mBookDetails.review,
            mBookDetails.description,
            mBookDetails.bookUrl,
            mBookDetails.imageUrl

            )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addFavouriteItem(this,addtoFavourites)
    }*/

    private fun addToFavourites(){
        val addtoFavourites= Books(
            FirestoreClass().getCurrentUserID(),
            mBookId,
            mBookDetails.title,
            mBookDetails.author,
            mBookDetails.category,
            mBookDetails.pages,
            mBookDetails.rating,
            mBookDetails.review,
            mBookDetails.description,
            mBookDetails.bookUrl,
            mBookDetails.imageUrl

        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addFavouriteItem(this,addtoFavourites)
    }


    fun addToFavouriteSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@BookDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_favourites),
            Toast.LENGTH_SHORT
        ).show()

        favourites.visibility=View.GONE
        favouritesRed.visibility=View.VISIBLE
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.favourites ->{
                    addToFavourites()
                }
                R.id.favouritesRed->{
                    Log.i("calling alert dialog",mBookId)
                    showAlertDialog(mBookId)
                }
            }
        }
    }

    private fun showAlertDialog(bookId: String) {
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Do you want to remove the book from Favourites?")

        alertDialog.setPositiveButton(
            "Delete",
        ) { dialogInterface, _ ->
            showProgressDialog(resources.getString((R.string.please_wait)))
            Log.i("inside ALERT POSITIVE",mBookId)
            FirestoreClass().deleteFavouriteItem(this,bookId)
            Log.i("DELETE ITEM CALLED",mBookId)
            dialogInterface.dismiss()
        }
        alertDialog.setNegativeButton(
            "Cancel",

        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    fun removalFomFavouritesSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.remove_from_book_success),
            Toast.LENGTH_SHORT
        ).show()

//        favourites.visibility=View.VISIBLE
//        favouritesRed.visibility=View.GONE
    }
}


