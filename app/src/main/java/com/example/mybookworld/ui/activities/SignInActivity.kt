@file:Suppress("DEPRECATION")

package com.example.mybookworld.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.mybookworld.R
import com.example.mybookworld.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

// TODO (Step 1: Add the Sign In activity.)
// START
class SignInActivity : BaseActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btn_sign_in.setOnClickListener(){
            signInRegisteredUser()
        }

        setupActionBar()
    }


    fun signInSuccess(user: User){
        hideProgressDialog()

        startActivity(Intent(this, MainActivity::class.java))

        finish()

    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser(){
        val email : String = et_email_sign_in.text.toString().trim{
            it <= ' '
        }
        val password: String = et_password_sign_in.text.toString().trim{
            it <= ' '
        }

        if(validateForm(email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        Log.d("Sign in" , "signInWIthEmail:success")
                        val user=auth.currentUser


                        startActivity(Intent(this, MainActivity::class.java))

                    } else {
                        Log.w("Sign in" , "signInWithEmail:failure",task.exception)
                        Toast.makeText(baseContext,"Authentication Failed. ", Toast.LENGTH_SHORT).show()

                    }
                }

        }
    }

    private fun validateForm(email:String, password: String) : Boolean{
        return when {

            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }
            else ->{
                true
            }
        }
    }
}
// END