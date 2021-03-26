package com.example.mybookworld.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mybookworld.R
import com.example.mybookworld.ui.adapters.EXTRA_MESSAGE
import kotlinx.android.synthetic.main.activity_pdf_viewer.*

class PdfViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        pdfView.fromUri(Uri.parse(message))
    }
}