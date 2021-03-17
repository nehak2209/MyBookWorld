package com.example.mybookworld.Firebase

import android.app.Activity
import android.util.Log
import com.example.mybookworld.activities.MainActivity
import com.example.mybookworld.activities.SignInActivity
import com.example.mybookworld.activities.SignUpActivity
import com.example.mybookworld.models.User
import com.example.mybookworld.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {


    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
                // Document ID for users fields. Here the document it is the User ID.
                .document(getCurrentUserID())
                // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {

                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error in writing document",
                            e
                    )
                }
    }


    fun signInUser(activity: Activity) {


        mFireStore.collection(Constants.USERS)

                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(
                            activity.javaClass.simpleName, document.toString()
                    )

                    // Here we have received the document snapshot which is converted into the User Data model object.
                    val loggedInUser = document.toObject(User::class.java)!!

                    when (activity){
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity ->{
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }

                }
                .addOnFailureListener {

                        e ->
                    when (activity){
                        is SignInActivity -> {
                            activity.hideProgressDialog()
                        }
                        is MainActivity ->{
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting loggedIn user details",
                            e
                    )
                }
    }


    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}