package com.example.mybookworld.ui.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.User
import com.example.mybookworld.utils.GlideLoader
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    companion object{
const val  MY_PROFILE_REQUEST_CODE:Int=11

        const val MY_FAVOURITE_REQUEST_CODE=22

    }

    private val mContext: Context? = null

    fun getContext(): Context? {
        return mContext
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
        setupBottomBar()
       nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.setNavigationOnClickListener {
            //Toggle Drawer
            toggleDrawer()

        }
    }

    private  fun toggleDrawer(){

        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

   fun updateNavigationUserDetails(user: User){
       GlideLoader(this).loadUserPicture(Uri.parse(user.image),nav_user_image)

       tv_username.text=user.name
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
                && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            // Get the user updated details.
            FirestoreClass().loadUserData(this@MainActivity)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_my_profile -> {

                startActivityForResult(Intent(this@MainActivity,
                        MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }

            R.id.nav_log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.nav_favourites -> {

             //   Toast.makeText(this@MainActivity, "favourite", Toast.LENGTH_SHORT).show()
             startActivityForResult(Intent(this@MainActivity,MyFavouriteActivity::class.java),
                   MY_FAVOURITE_REQUEST_CODE)
            }
            R.id.nav_about -> {
                val aboutIntent = Intent(this@MainActivity,AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            R.id.nav_help -> {
                val helpIntent = Intent(this@MainActivity,Help::class.java)
                startActivity(helpIntent)
            }


        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun setupBottomBar()
    {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

}