package com.example.mybookworld.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Books(
        var book_id: String = "",
        val title: String ="",
        val author: String ="",
        val imageUrl: String ="",
        val bookUrl: String ="",
        val pages: String ="",
        val rating: String  ="",
        val review: String  ="",
        val description: String  ="",
        val category: String=""
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(book_id)
                parcel.writeString(title)
                parcel.writeString(author)
                parcel.writeString(imageUrl)
                parcel.writeString(bookUrl)
                parcel.writeString(pages)
                parcel.writeString(rating)
                parcel.writeString(review)
                parcel.writeString(description)
                parcel.writeString(category)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Books> {
                override fun createFromParcel(parcel: Parcel): Books {
                        return Books(parcel)
                }

                override fun newArray(size: Int): Array<Books?> {
                        return arrayOfNulls(size)
                }
        }
}