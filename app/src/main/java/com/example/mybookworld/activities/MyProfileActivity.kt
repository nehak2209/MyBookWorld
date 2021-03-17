package com.example.mybookworld.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.User
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.IOException

class MyProfileActivity : BaseActivity() {


    companion object{
        private const val READ_STORAGE_PERMISSION_CODE=1
        private  const val PICK_IMAGE_REQUEST_CODE=2
    }

    private  var mSelectedImageFileUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
        FirestoreClass().loadUserData(this)
        iv_profile_user_image.setOnClickListener {
             if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                     ==PackageManager.PERMISSION_GRANTED){
                 showImageChoose()


             }else{
                 ActivityCompat.requestPermissions(
                         this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                         READ_STORAGE_PERMISSION_CODE
                 )
             }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showImageChoose()
            }
        }else{

            Toast.makeText(this,"oops , you just denied  the permission for storage." +
                    " you can allow it from settings",Toast.LENGTH_LONG).show()
        }

    }
    private fun showImageChoose(){

        var galleryIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK
                && requestCode== PICK_IMAGE_REQUEST_CODE
                && data!!.data!=null){
            mSelectedImageFileUri=data.data
            try {
                Glide
                        .with(this@MyProfileActivity)
                        .load(mSelectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(iv_profile_user_image)
            }catch (e:IOException){
                e.printStackTrace()
            }


        }
    }
    private  fun setupActionBar(){
   setSupportActionBar(toolbar_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24dp)
            actionBar.title=resources.getString(R.string.my_profile_title)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun setUserDataInUI(user: User){
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)
        et_email.setText(user.email)
        et_name.setText(user.name)
        if(user.mobile!=0L){
    et_mobile.setText(user.mobile.toString())
        }

    }

}