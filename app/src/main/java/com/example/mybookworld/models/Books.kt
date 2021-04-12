package com.example.mybookworld.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Books(
      /*  var book_id: String = "",
        val title: String ="",
        val author: String ="",
        val imageUrl: String ="",
        val bookUrl: String ="",
        val pages: String ="",
        val rating: String  ="",
        val review: String  ="",
        val description: String  ="",
        val category: String="",
        val user_id:String=""*/
        val user_id:String="",
        var book_id:String="",
        val title:String="",
        val author:String="",
        val imageUrl:String="",
        val bookUrl:String="",
        val pages:String="",
        val rating:String="",
        val review:String="",
        val description:String="",
        val category:String=""

):Parcelable {

}