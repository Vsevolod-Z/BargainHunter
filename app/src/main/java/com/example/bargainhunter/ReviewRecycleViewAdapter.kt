package com.example.bargainhunter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bargainhunter.models.Achievements
import com.example.bargainhunter.models.Highlighted
import com.example.bargainhunter.models.Movies
import com.example.bargainhunter.models.Review
import java.lang.Math.abs


class ReviewRecycleViewAdapter(private var con: Context, var review: List<Review>,var rcv:RecyclerView) : RecyclerView.Adapter<ReviewRecycleViewAdapter.ReviewViewHolder>(){



    private lateinit var view: View

    val context:Context

    init{

        context = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.review_recycleview_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount() = review.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.tvReview.text = review[position].review
        holder.tvReviewNum.text = view.context.getString(R.string.review_num,position+1)


    }


    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tvReview = itemView.findViewById<TextView>(R.id.tvReview)
        var tvReviewNum = itemView.findViewById<TextView>(R.id.tvReviewNum)

    }
}