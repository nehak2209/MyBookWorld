package com.example.mybookworld.Firebase

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mybookworld.models.Books
import com.example.mybookworld.models.User
import com.example.mybookworld.models.myBooks
import com.example.mybookworld.ui.activities.*
import com.example.mybookworld.ui.fragments.CategoryWiseBooksFragment
import com.example.mybookworld.ui.fragments.HomeFragment
import com.example.mybookworld.ui.fragments.WriterSectionFragment
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



    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS) // Collection Name
                .document(getCurrentUserID()) // Document ID
                .update(userHashMap) // A hashmap of fields which are to be updated.
                .addOnSuccessListener {
                    // Profile data is updated successfully.
                    Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")

                    Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                    // Notify the success result.
                    activity.profileUpdateSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while creating a board.",
                            e
                    )
                }
    }

    fun loadUserData(activity: Activity) {

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
                        is MyProfileActivity->{
                            activity.setUserDataInUI(loggedInUser)
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

    fun getBooksList(fragment: Fragment){
      mFireStore.collection(Constants.BOOKS)
            .get()
            .addOnSuccessListener {
                document->
                Log.e("books list",document.documents.toString())
                val bookList:ArrayList<Books> = ArrayList()
                for ( i in document.documents){
                    val book=i.toObject(Books::class.java)
                    book!!.book_id=i.id
                    bookList.add(book)

                }

                when(fragment){
                    is HomeFragment->{

                        fragment.successProductListFromFireStore(bookList)
                    }
                }
            }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }


    }


    fun uploadUserBookDetails(FragmentActivity:WriterSectionFragment,bookInfo: myBooks){
        mFireStore.collection(Constants.USERBOOKS)
                .document()
                .set(bookInfo, SetOptions.merge())
                .addOnSuccessListener {
                    FragmentActivity.userBookCoverUploadSuccess()
                }.addOnFailureListener{e->
                BaseActivity().hideProgressDialog()
                Log.e(
                    FragmentActivity.javaClass.simpleName, "Error while uploading book to cloud storage.",
                    e
                )
            }
    }
    fun getGenreBooksList(fragment: Fragment,genre: String){
        mFireStore.collection(Constants.BOOKS)
                .whereEqualTo("category",genre)
                .get()
                .addOnSuccessListener {
                    document->
                    Log.e("books list",document.documents.toString())
                    val bookList:ArrayList<Books> = ArrayList()
                    for ( i in document.documents){
                        val book=i.toObject(Books::class.java)
                        book!!.book_id=i.id
                        bookList.add(book)

                    }

                    when(fragment){
                        is CategoryWiseBooksFragment ->{
                            fragment.successCategoryWiseListFromFireStore(bookList)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }

}


