package com.example.mybookworld.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (

    val id: String = "",
    val name: String="",
    val email: String="",
    val image:String="",
    val mobile: Long=0,
    val fcmToken: String=""

): Parcelable {
    @Parcelize
    data class UserBooks(
            var user_id : String="",
            var user_name : String ="",
            //var book_id: String = "",
            val title: String ="",
            val author: String ="",
            val imageUrl: String ="",
            val bookUrl: String ="",
            val pages: String ="",
            // val bookPdf: String="",
            val description: String  ="",
            val category: String=""
    ):Parcelable
}
