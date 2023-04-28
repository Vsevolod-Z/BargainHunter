package com.example.bargainhunter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bargainhunter.models.Movies


class VideoRecycleViewAdapter(private var con: Context, var videos: List<Movies>,var videoView: VideoView) : RecyclerView.Adapter<VideoRecycleViewAdapter.VideoViewHolder>(){



    private lateinit var view: View

    val context:Context

    init{

        context = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.video_recycleview_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        GameDataPreparer.imageDownloadAndSet(holder.img,videos[position].thumbnail,context)
        holder.appCard.setOnClickListener{
          GameDataPreparer.videoDownload(videoView,videos[position].mp4.lq,context)
        }

    }


    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img = itemView.findViewById<ImageView>(R.id.screenshotImageView)
        var appCard = itemView.findViewById<CardView>(R.id.appCard)

    }
}