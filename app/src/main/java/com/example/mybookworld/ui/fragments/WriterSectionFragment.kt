@file:Suppress("Annotator")

package com.example.mybookworld.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.Books

import com.example.mybookworld.utils.Constants
import com.example.mybookworld.utils.GlideLoader
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_writer_section.*
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WriterSectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class WriterSectionFragment : Fragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRootView: View

    private var mSelectedImageFIleUri :Uri? = null
    private var mBookUri : Uri? = null
    private lateinit var mProgressDialog: Dialog
    private var mBookCoverImageURL: String = ""
    private var mBookURL: String = ""
    private var bookCategory:String=""
    private var book_id:String=""
    private var rating:String=""
    private var review:String=""

    private lateinit var userBook:Books
    private val userBookFireStore = FirebaseFirestore.getInstance()

    var pdf:Int=0;
    var uriTxt : Uri?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View? = inflater.inflate(R.layout.fragment_writer_section, container, false)

        val image : ImageView? = view?.findViewById(R.id.add_book_cover_photo)

            image!!.setOnClickListener(this)


        val submitBook : Button? = view?.findViewById(R.id.btn_submit)

            submitBook!!.setOnClickListener(this)

        val uploadPdfButton : Button? = view?.findViewById(R.id.btn_upload_book)

        uploadPdfButton!!.setOnClickListener(this)

        val radioGroup :RadioGroup = view?.findViewById(R.id.til_book_category)

        radioGroup.setOnCheckedChangeListener(this)

        return view
    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
              when(checkedId){
                  R.id.til_romance -> {
                      bookCategory="Romance"
                  }
                  R.id.til_horror -> {
                      bookCategory="Horror"
                  }
                  R.id.til_fiction -> {

                  bookCategory="Fiction"

                 }
                  R.id.til_fantasy -> {

                  bookCategory="Fantasy"

                 }
                  R.id.til_historical_fiction -> {

                  bookCategory="Historical Fiction"

                 }
                  R.id.til_action_adventure -> {

                  bookCategory="Action And Adventure"
                 }
                  R.id.til_biography -> {
                      bookCategory="Romance"
                  }
                  R.id.til_health -> {
                      bookCategory="Health and Fitness"
                  }
              }
    }


    //Function to execute onCLic actions
    override fun onClick(v: View?){
        if(v !=null){
            when(v.id) {
                R.id.add_book_cover_photo -> { //imageView to add book cover photo in app
                    if (ContextCompat.checkSelfPermission(requireContext().applicationContext,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            == PackageManager.PERMISSION_GRANTED) {
                        showImageChooser()
                    } else {
                        ActivityCompat.requestPermissions(requireContext() as Activity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btn_submit -> { //button to submit all data related to book to cloud storage
                    if (validateBookDetails()) {
                        bookCoverUpload()
                    }
                }
                R.id.btn_upload_book -> {
                    Log.d("upload book pdf", "upload book pdf to cloud")
                    if (ContextCompat.checkSelfPermission(requireContext().applicationContext,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            == PackageManager.PERMISSION_GRANTED) {
                        showPdfChooser()
                    } else {
                        ActivityCompat.requestPermissions(requireContext() as Activity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
            }
        }
    }


    private fun showImageChooser() {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun showPdfChooser(){
        //view:View? -> val intent = Intent()
        val intent = Intent()
       // intent.setType("pdf/*")
        //intent.type = "application/pdf/*"
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select pdf"), pdf)
    }


    //Function to take permission of user to choose image from phone
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                        activity,
                        "Oops, you just denied the permission for storage. You can also allow it from settings.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    //Function to select and display image  of book cover in app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(resultCode==Activity.RESULT_OK) {
        Log.i("data 1", data.toString())
        if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
            Log.i("data 2", data.toString())
            if (data != null) {
                Log.i("data 3", data.toString())
                add_book_cover_photo.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_vector_edit))

                mSelectedImageFIleUri = data.data!!

                try {
                    GlideLoader(requireContext()).loadUserPicture(mSelectedImageFIleUri!!, book_cover)
                } catch (e: IOException) {

                    e.printStackTrace()
                }
            }
        } else if (requestCode == pdf) {
            if (data!= null){
                Log.i("data 3", data.toString())
                mBookUri = data.data!!
            }

            //bookUri = data!!.data!!
            uriTxt = mBookUri
            uploadPdfToCLoudStorage()
        }

    } else if(resultCode == Activity.RESULT_CANCELED){
        Log.e("Request Cancelled", "Image Selection Cancelled")
        }

    }

    //Function to uploadbook cover image to cloud storage
    private fun uploadBookCoverToCloudStorage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFIleUri != null) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                    "BOOK_COVER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                            mSelectedImageFIleUri
                    )
            )
            //adding the file to reference
            sRef.putFile(mSelectedImageFIleUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        // The image upload is success
                        Log.e(
                                "Firebase Image URL",
                                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                        )

                        // Get the downloadable url from the task snapshot
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener { uri ->
                                    Log.e("Downloadable Image URL", uri.toString())

                                    // assign the image url to the variable.
                                    mBookCoverImageURL = uri.toString()

                                    imageUploadSuccess(mBookCoverImageURL)

                                    // Call a function to update user details in the database.
                                    userBookCoverUploadSuccess()
                                }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                                requireContext(),
                                exception.message,
                                Toast.LENGTH_LONG
                        ).show()

                        hideProgressDialog()
                    }
        }
    }

    private fun uploadPdfToCLoudStorage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mBookUri != null) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                    "BOOK_PDF" + System.currentTimeMillis() + ".pdf"// + getFileExtension(
                    //bookUri
                    //)
            )

            //adding the file to reference
            sRef.putFile(mBookUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        // The image upload is success
                        Log.e(
                                "Firebase Book URL",
                                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                        )

                        // Get the downloadable url from the task snapshot
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener { uri ->
                                    Log.e("Downloadable Book URL", uri.toString())

                                    // assign the image url to the variable.
                                    mBookURL = uri.toString()
                                    Log.e("book url: ", mBookURL)

                                    bookUploadSuccess(mBookURL)

                                    // Call a function to update user details in the database.
                                    //userBookCoverUploadSuccess()
                                }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                                requireContext(),
                                exception.message,
                                Toast.LENGTH_LONG
                        ).show()

                        hideProgressDialog()
                    }
        }else{
            showErrorSnackBar(resources.getString(R.string.err_msg_select_book), true)
            false
        }
    }

    //Function to validate book details entered by user
    private fun validateBookDetails():Boolean{
        return when{
            mSelectedImageFIleUri == null ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_book_cover), false)
                false
            }
            mBookUri == null ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_book), false)
                false
            }
            bookCategory == null ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_category), false)
                false
            }
            TextUtils.isEmpty(et_author_name.text.toString().trim {
                it <= ' '
            }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), false)
                false
            } TextUtils.isEmpty(et_book_title.text.toString().trim {
                it <= ' '
            }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_name), false)
                false
            }TextUtils.isEmpty(et_book_description.text.toString().trim {
                it <= ' '
            }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_desc), false)
                false
            }TextUtils.isEmpty(et_book_pages.text.toString().trim {
                it <= ' '
            }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_pages), false)
                false
            }else ->{
                true
            }
        }
    }



    //function to update book data uploaded by user
    fun userBookCoverUploadSuccess() {
        val userName = this.requireContext().
        getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE)?.
        getString(Constants.LOGGED_IN_USERNAME, "")!!


       /* bookDetail = myBooks(     //myBooks is data class
                FirestoreClass().getCurrentUserID(),
                userName,
                et_book_title.text.toString().trim { it <= ' ' },
                et_author_name.text.toString().trim { it <= ' ' },
                mBookCoverImageURL,
                mBookURL,
                et_book_pages.text.toString().trim { it <= ' ' },
                et_book_description.text.toString().trim { it <= ' ' },
                bookCategory,
                book_id
        )*/
         userBook = Books(
             book_id,
             et_book_title.text.toString().trim { it <= ' ' },
             et_author_name.text.toString().trim { it <= ' ' },
             mBookCoverImageURL,
             mBookURL,
             et_book_pages.text.toString().trim { it <= ' ' },
             rating,
             review,
             et_book_description.text.toString().trim { it <= ' ' },
             bookCategory,
             FirestoreClass().getCurrentUserID(),
         )

       // FirestoreClass().uploadUserBookDetails(this,bookDetail)
        uploadUserBookDetails()
    }

   /* private fun uploadUserBookDetails(){

        userBookFireStore.collection(Constants.USERBOOKS)
                .document()
                .set(bookDetail, SetOptions.merge())
                .addOnSuccessListener {

                    showUserBookUploadSuccess()


                }.addOnFailureListener{ e->
                    hideProgressDialog()
                   // Log.e(
                        //    FragmentActivity.javaClass.simpleName, "Error while uploading book to cloud storage.",
                        //    e
                    //)
                }
    }*/
   private fun uploadUserBookDetails(){

       userBookFireStore.collection(Constants.BOOKS)
           .document()
           .set(userBook, SetOptions.merge())
           .addOnSuccessListener {

               showUserBookUploadSuccess()


           }.addOnFailureListener{ e->
               hideProgressDialog()
               // Log.e(
               //    FragmentActivity.javaClass.simpleName, "Error while uploading book to cloud storage.",
               //    e
               //)
           }
   }

    //Function to display success of book upload
    fun showUserBookUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
                requireContext(), resources.getString(R.string.user_book_upload_success),
                Toast.LENGTH_SHORT
        ).show()
        activity?.finish()
        activity?.startActivity(activity?.intent)
    }

    //Function to show progress dialog
    private fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireContext())

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        //Start the dialog and display it on screen.
         mProgressDialog.show()
    }

    //Function to hide progress dialog
    private fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    //Function to get fileExtension of file chosen
    private fun getFileExtension(uri: Uri?): String? {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity?.contentResolver?.getType(uri!!))//.toString())
    }


    //function to call uploadBookCoverToCloudStorage function
    private fun bookCoverUpload(){
        showProgressDialog(resources.getString(R.string.please_wait))

        uploadBookCoverToCloudStorage()
    }



    //function to display success of book cover image uploading to storage
    private fun imageUploadSuccess(imageUrl: String) {

        mBookCoverImageURL=imageUrl

        showUserBookUploadSuccess()

    }

    //function to display success of book cover image uploading to storage
    private fun bookUploadSuccess(bookUrl: String) {
        hideProgressDialog()

        mBookURL=bookUrl

        //showUserBookCoverUploadSuccess()

    }



    //SnackBar to diaplay messages
    private fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar : Snackbar =
                Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSnackBarError
                    )
            )
        }else{
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSnackBarSuccess
                    )
            )
        }
        snackBar.show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 2
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WriterSectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WriterSectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}