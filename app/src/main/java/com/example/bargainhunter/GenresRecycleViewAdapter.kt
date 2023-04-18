package com.example.bargainhunter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import androidx.core.content.res.ResourcesCompat
import com.example.bargainhunter.models.App
import com.example.bargainhunter.models.Genres


class GenresRecycleViewAdapter(private  val con: Context,fragment: GameListFragment) : RecyclerView.Adapter<GenresRecycleViewAdapter.GenreViewHolder>(){
    val context:Context = con
    val fragment:GameListFragment = fragment
     private var genres: MutableList<Genres> = mutableListOf()
    var isLoading = false

    var selectedGenres: MutableList<Genres> = mutableListOf()

    public fun addGenres(genreList: MutableList<Genres>){
    genres.addAll( genreList )
        Log.d("genres","genres item count: " + itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        // Создаем ViewHolder для элемента списка
        val view = LayoutInflater.from(parent.context).inflate(R.layout.genres_recycleview_item, parent, false)

        return GenreViewHolder(view)
    }

    override fun getItemCount() = genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        // Заполняем ViewHolder данными из списка

        holder.title.text = genres[position].description

        holder.card.setOnClickListener {

            if( genres[position] in selectedGenres){
                selectedGenres.removeAll(setOf(genres[position]))
                holder.linearLayout.setBackgroundResource(R.drawable.tab_background)
                fragment.ResetPageCount(1)
            }else{
                selectedGenres.add(genres[position])
                holder.linearLayout.setBackgroundColor(R.drawable.card_background)
                fragment.ResetPageCount(1)
            }
        }

    }


    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title = itemView.findViewById<TextView>(R.id.tvGenreTitle)
        var card = itemView.findViewById<CardView>(R.id.genreCard)
        var linearLayout = itemView.findViewById<LinearLayout>(R.id.genreLayout)


    }
}