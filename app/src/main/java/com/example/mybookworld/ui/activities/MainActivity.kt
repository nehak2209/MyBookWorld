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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.ui.fragments.GenreFragment
import com.example.mybookworld.ui.fragments.HomeFragment
import com.example.mybookworld.ui.fragments.MyWorksFragment
import com.example.mybookworld.ui.fragments.WriterSectionFragment
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

//       Glide.with(this@MainActivity)
//               .load(user.image)
//               .centerCrop()
//               .placeholder(R.drawable.ic_user_place_holder)
//               .into(nav_user_image)

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
                Toast.makeText(this@MainActivity, "favourites", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {

                Toast.makeText(this@MainActivity, "about", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_help -> {

                Toast.makeText(this@MainActivity, "help", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my_works -> {

                Toast.makeText(this@MainActivity, "setting", Toast.LENGTH_SHORT).show()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }
    private fun setupBottomBar()
    {
        val homeFragment = HomeFragment()
        val categoryFragment = GenreFragment()
        val myBooksFragment = MyWorksFragment()
        val writerSectionFragment = WriterSectionFragment()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        makeCurrentFragment(homeFragment)
        bottomNav.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_explore -> makeCurrentFragment(categoryFragment)
                R.id.ic_list -> makeCurrentFragment(myBooksFragment)
                R.id.ic_write -> makeCurrentFragment(writerSectionFragment)
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper, fragment)
            commit()
        }




}