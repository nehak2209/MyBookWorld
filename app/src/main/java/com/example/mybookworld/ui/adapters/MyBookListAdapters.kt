package com.example.mybookworld.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mybookworld.R
import com.example.mybookworld.models.Books
import com.example.mybookworld.ui.activities.BookDetailsActivity
import com.example.mybookworld.ui.activities.MyFavouriteActivity
import com.example.mybookworld.ui.activities.PdfReaderActivity
import com.example.mybookworld.ui.fragments.HomeFragment
import com.example.mybookworld.utils.Constants
import com.example.mybookworld.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

const val EXTRA_MESSAGE = "com.example.MyBookWorld.MESSAGE"




open class MyBookListAdapters(
    private val context: Context,
    private var list: ArrayList<Books>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     return MyViewHolder(
         LayoutInflater.from(context).inflate(
             R.layout.item_list_layout,
             parent,
             false
         )
     )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model=list[position]
        if(holder is MyViewHolder){
          GlideLoader(context).loadBookPicture(
              Uri.parse(model.imageUrl),
              holder.itemView.book_image
          )
           holder.itemView.title.text=model.title
            holder.itemView.book_author.text=model.author
            holder.itemView.book_pagesrev.text="Pages:" + "${model.pages}"
            holder.itemView.review.text="Reviews:"+"${model.review}"
            holder.itemView.score.text=model.rating

            when(context){
                is MyFavouriteActivity -> {
                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, PdfReaderActivity::class.java)
                        intent.putExtra("url", model.bookUrl)
                        intent.putExtra("bookName", model.title)
                        context.startActivity(intent)
                    }
                }
                 else ->{
                     holder.itemView.setOnClickListener {
                         val intent = Intent(context, BookDetailsActivity::class.java)
                         intent.putExtra(Constants.EXTRA_BOOK_ID, model.book_id)
                         context.startActivity(intent)
                     }
                 }

            }


        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

  class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}