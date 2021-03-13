package com.example.mybookworld.Firebase

import android.util.Log
import com.example.mybookworld.activities.SignInActivity
import com.example.mybookworld.activities.SignUpActivity
import com.example.mybookworld.models.User
import com.example.mybookworld.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {


    private val mFirestore= FirebaseFirestore.getInstance()


    fun registerUser(activitiy:SignUpActivity, userInfo: User){
        mFirestore.collection(Constants.USERS)
                .document(getCurrentUserId()).
                set(userInfo, SetOptions.merge()).
                addOnFailureListener{
                    activitiy.userRegisteredSuccess()
                }.addOnFailureListener{
                    e->
                    Log.e(activitiy.javaClass.simpleName,"Error")
                }

    }

    fun signInUser(activity: SignInActivity){

    }

    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}