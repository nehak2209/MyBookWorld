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

        // TODO (Step 6: Add the full screen flags here.)
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        // START
        window.setFlags(

                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // END

        // TODO (Step 7: Add the file in the custom font file to the assets folder. And add the below line of code to apply it to the title TextView.)
        // Steps for adding the assets folder are :
        // Right click on the "app" package and GO TO ==> New ==> Folder ==> Assets Folder ==> Finish.
        // START
        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface: Typeface =
                Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeface

        Handler().postDelayed({

            var currentUserId= FirestoreClass().getCurrentUserId()

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
