package com.example.bargainhunter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bargainhunter.models.Screenshots


class ScreenshotRecycleViewAdapter(private var con: Context, var screenshots: List<Screenshots>) : RecyclerView.Adapter<ScreenshotRecycleViewAdapter.ScreenShotsViewHolder>(){



    private lateinit var view: View

    val context:Context

    init{

        context = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenShotsViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.screenshot_recycleview_item, parent, false)
        return ScreenShotsViewHolder(view)
    }

    override fun getItemCount() = screenshots.size

    override fun onBindViewHolder(holder: ScreenShotsViewHolder, position: Int) {
        GameDataPreparer.imageDownloadAndSet(holder.img,screenshots[position].path_thumbnail,context)
        val density = context.resources.displayMetrics.density
        val dpWidth = holder.appCard.width / density
        val dpHeight = holder.appCard.height / density
        Log.d("screenshots",String.format("width:%s,height%s",dpWidth,dpHeight))

    }


    class ScreenShotsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img = itemView.findViewById<ImageView>(R.id.screenshotImageView)
        var appCard = itemView.findViewById<CardView>(R.id.appCard)

    }
}