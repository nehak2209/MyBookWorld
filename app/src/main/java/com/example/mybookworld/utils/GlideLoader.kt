package com.example.mybookworld.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mybookworld.R
import java.io.IOException

class GlideLoader(val context: Context){

    fun  loadUserPicture(imageURI: Uri,imageView: ImageView){

        try {
            Glide.with(context)
                    .load(imageURI)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(imageView)
        }catch (e:IOException){
            e.printStackTrace()
        }

    }

    fun  loadBookPicture(imageURI: Uri,imageView: ImageView){

        try {
            Glide.with(context)
                .load(imageURI)
                .centerCrop()
                .into(imageView)
        }catch (e:IOException){
            e.printStackTrace()
        }

    }
}
