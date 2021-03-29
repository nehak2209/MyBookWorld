package com.example.mybookworld.ui.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mybookworld.R;

import com.github.barteksc.pdfviewer.PDFView;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewerActivity extends AppCompatActivity {


    PDFView pdfView;
    private String url;
    private  String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Bundle bundle = getIntent ().getExtras();
        if (bundle != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("bookName");

        }
        setupActionBar();
        Log.i("url", url);

        // initializing our pdf view.
        pdfView = findViewById(R.id.idPDFView);
        new RetrivePDFfromUrl ().execute(url);
    }

    private void setupActionBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_my_viewer_activity);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24dp);
            Log.i("bookName",title);
            actionBar.setTitle(title);
        }


        myToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
        // we are using inputstream
        // for getting out PDF.
        InputStream inputStream = null;
        try {
            URL url = new URL(strings[0]);
            // below is the step where we are
            // creating our connection.
            HttpURLConnection urlConnection =(HttpsURLConnection) url . openConnection ();
            if (urlConnection.getResponseCode() == 200) {
                // response is success.
                // we are getting input stream from url
                // and storing it in our variable.
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }

        } catch (IOException e) {
            // this is the method
            // to handle errors.
            e.printStackTrace();
            return null;
        }
        return inputStream;
    }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }
}
