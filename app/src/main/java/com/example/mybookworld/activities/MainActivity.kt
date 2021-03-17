package com.example.mybookworld.activities


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.mybookworld.Firebase.FirestoreClass
import com.example.mybookworld.R
import com.example.mybookworld.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
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
       Glide
               .with(this)
               .load(user.image)
               .centerCrop()
               .placeholder(R.drawable.ic_user_place_holder)
               .into(nav_user_image)

       tv_username.text=user.name
   }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_my_profile -> {

                startActivity(Intent(this,MyProfileActivity::class.java))
            }

            R.id.nav_log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.nav_favourites->{
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



}