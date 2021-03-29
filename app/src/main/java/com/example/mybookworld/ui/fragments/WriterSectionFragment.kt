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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.myBooks
import com.example.mybookworld.utils.Constants
import com.example.mybookworld.utils.GlideLoader
import com.example.mybookworld.utils.PermissionConstants
import com.google.android.material.snackbar.Snackbar
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
class WriterSectionFragment : Fragment(), View.OnClickListener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //val pdf:Int=0;
    //lateinit var uri: Uri
    //lateinit var mStorage: StorageReference
   private var mSelectedImageFIleUri :Uri? = null
    private lateinit var mProgressDialog: Dialog
    private var mBookCoverImageURL: String = ""
    private var mBookURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //btn_upload_book.setOnClickListener(){
          //  uploadBookPdf()
        //}
        //val image : ImageView = findViewById(R.id.add_book_cover_photo)

      // var image = requireView().findViewById<ImageView>(R.id.add_book_cover_photo)
       // image.setOnClickListener { Log.d(TAG, "onViewCreated(): hello world");
        //    image.setOnClickListener(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View? = inflater.inflate(R.layout.fragment_writer_section, container, false)

        val image : ImageView? = view?.findViewById(R.id.add_book_cover_photo)
        //if (image != null) {
            image?.setOnClickListener(this)
        //}

       val submitBook : Button? = view?.findViewById(R.id.btn_submit_book)
        //if (submitBook != null) {
            submitBook?.setOnClickListener(this)
        //}
        //image.setOnClickListener { Log.d("click ok", "onViewCreated(): hello world");
        //image.setOnClickListener(this)
        return view
    }

    fun showImageChooser(FragmentActivity: WriterSectionFragment) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        getActivity()?.startActivityForResult(galleryIntent, PermissionConstants.PICK_IMAGE_REQUEST_CODE)
    }

//Function to execute onClick actions
    override fun onClick(v: View?){
        if(v !=null){
            when(v.id) {
                R.id.add_book_cover_photo -> { //imageView to add book cover photo in app
                    if (ContextCompat.checkSelfPermission(getActivity()!!.getApplicationContext(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(getActivity()!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PermissionConstants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.btn_submit_book ->{             //button to submit all data related to book to cloud storage
                    if(ValidateBookDetails()){
                        uploadBookCover()
                    }
                }
            }
        }
    }

    //Function to take permission of user to choose image from phone
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionConstants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                        getActivity(),
                        resources.getString(R.string.read_storage_permission_denied),
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //Function to select and display image  of book cover in app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK
            && requestCode == PermissionConstants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
    ) {

        // Replace the add icon with edit icon once the image is selected.
        add_book_cover_photo.setImageDrawable(
                ContextCompat.getDrawable(
                        getActivity()!!,
                        R.drawable.ic_vector_edit
                )
        )

        // The uri of selection image from phone storage.
        mSelectedImageFIleUri = data.data!!

        try {
            // Load the product image in the ImageView.
            GlideLoader(getActivity()!!).loadBookPicture(
                    mSelectedImageFIleUri!!,
                    iv_book_cover_image
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    }

   //Function to validate book details entered by user
    private fun ValidateBookDetails():Boolean{
        return when{
            (mSelectedImageFIleUri == null) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_book_cover),true)
                false
            }
            /*TextUtils.isEmpty(et_name.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name),false)
                false
            } TextUtils.isEmpty(et_email.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), false)
                false
            } */
            TextUtils.isEmpty(et_book_name.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_name), false)
                false
            }TextUtils.isEmpty(et_book_category.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_category), false)
                false
            }TextUtils.isEmpty(et_description_book.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_desc), false)
                false
            }TextUtils.isEmpty(et_pages.text.toString().trim{
                it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_pages), false)
                false
            }else ->{
                true
            }
        }
    }

    private fun uploadBookCover(){
        showProgressDialog("Please Wait...")
        uploadBookCoverToCloudStorage(this, mSelectedImageFIleUri!!)

    }

    //function to display success of book cover image uploading to storage
    fun imageUploadSuccess(imageUrl : String) {

        //hideProgressDialog()
        //showErrorSnackBar("Book Cover Image Is Uploaded Succcessfully, image URL: $imageUrl",false)
        mBookCoverImageURL=imageUrl

        uploadUserBookDetails()         //write this function in bookpdfuploadsuccess
    }

    //function to update book data uploaded by user
    private fun uploadUserBookDetails() {
        val userName = this.getActivity()?.
        getSharedPreferences(Constants.MYBOOKWORLD_PREFERENCES , Context.MODE_PRIVATE)?.
        getString(Constants.LOGGED_IN_USERNAME ," ")!!


        val bookDetail = myBooks(     //myBooks is data class
                FirestoreClass().getCurrentUserID(),
                userName,
                et_book_name.text.toString().trim { it <= ' ' },
                userName,
                mBookCoverImageURL,
                mBookURL,
                et_pages.text.toString().trim { it <= ' ' },
                et_book_category.text.toString().trim { it <= ' ' },
                et_description_book.text.toString().trim { it <= ' ' }
        )

        FirestoreClass().uploadUserBookDetails(this,bookDetail)
    }

//Function to display success of book upload
  fun userBookCoverUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
                getActivity()!!,resources.getString(R.string.user_book_upload_success),
                Toast.LENGTH_SHORT
        ).show()
       getActivity()!!.finish()
    }



    //Function to show progress dialog
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(getActivity()!!)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    //Function to hide progress dialog
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    //Function to get fileExtension of file chosen
    private fun getFileExtension(uri: Uri?): String? {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(
                getActivity()?.getContentResolver()?.getType(uri!!))//.toString())
    }

    //SnackBar to diaplay messages
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar : Snackbar =
                Snackbar.make(getActivity()!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            getActivity()!!,
                            R.color.colorSnackBarError
                    )
            )
        }else{
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            getActivity()!!,
                            R.color.colorSnackBarSuccess
                    )
            )
        }
        snackBar.show()
    }




    //function to call uploadBookCoverToCloudStorage function


    //Function to uploadbook cover image to cloud storage
    private fun uploadBookCoverToCloudStorage(FragmentActivity: WriterSectionFragment, uploadImageUri: Uri?) {
        showProgressDialog(resources.getString(R.string.please_wait))

        //if (mSelectedImageFIleUri != null) {
            println("**************************" +
                    "**************************" +
                    "************************")

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
                                    uploadUserBookDetails()
                                }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                                getActivity(),
                                exception.message,
                                Toast.LENGTH_LONG
                        ).show()

                        hideProgressDialog()
                    }
       // }
    }





    companion object {
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


