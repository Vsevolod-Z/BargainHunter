package com.example.bargainhunter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bargainhunter.models.Achievements
import com.example.bargainhunter.models.Highlighted
import com.example.bargainhunter.models.Movies


class AchievementsRecycleViewAdapter(private var con: Context, var highlighted: List<Highlighted>) : RecyclerView.Adapter<AchievementsRecycleViewAdapter.AchievementsViewHolder>(){



    private lateinit var view: View

    val context:Context

    init{

        context = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.achievements_item, parent, false)
        return AchievementsViewHolder(view)
    }

    override fun getItemCount() = highlighted.size

    override fun onBindViewHolder(holder: AchievementsViewHolder, position: Int) {
        GameDataPreparer.imageDownloadAndSet(holder.img,highlighted[position].path,context)
        holder.tvAchievement.text = highlighted[position].name
    }


    class AchievementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img = itemView.findViewById<ImageView>(R.id.achievementImage)
        var tvAchievement = itemView.findViewById<TextView>(R.id.tvAchievement)

    }
}