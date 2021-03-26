package com.example.mybookworld.models

import android.os.Parcel
import android.os.Parcelable

data class Books (
        val bId:String="",
        val bName:String="",
        val bGenre:String,
        val authorID:String=""

   ):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bId)
        parcel.writeString(bName)
        parcel.writeString(bGenre)
        parcel.writeString(authorID)
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

