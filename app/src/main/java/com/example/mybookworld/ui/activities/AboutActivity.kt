package com.example.mybookworld.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mybookworld.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupActionBar()
    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_activity_about)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24dp)
            actionBar.title = resources.getString(R.string.nav_about)
        }

        toolbar_activity_about.setNavigationOnClickListener { onBackPressed() }
    }
}
