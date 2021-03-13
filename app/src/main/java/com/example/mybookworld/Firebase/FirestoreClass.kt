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
                addOnSuccessListener{
                    activitiy.userRegisteredSuccess()
                }.addOnFailureListener{
                    e->
                    Log.e(activitiy.javaClass.simpleName,"Error")
                }

    }

    fun signInUser(activity: SignInActivity){
        mFirestore.collection(Constants.USERS)
                .document(getCurrentUserId()).
                get().
                addOnSuccessListener{document ->

                    val loggedInUser=document.toObject(User::class.java)!!
                    if(loggedInUser != null)
                        activity.signInSuccess(loggedInUser)

                }.addOnFailureListener{
                    e->
                    Log.e("signInUser","Error")
                }

    }

    fun getCurrentUserId():String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if(currentUserId != null){
            currentUserId = currentUser.uid
        }
        return currentUserId

    }
}