@file:Suppress("DEPRECATION")

package com.example.mybookworld.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_splash)


        window.setFlags(

                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeface: Typeface =
                Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeface

        Handler().postDelayed({

            var currentUserId= FirestoreClass().getCurrentUserID()

            if(currentUserId.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
           }
            else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
                finish()
        },3000)
            }
        // END
    }
