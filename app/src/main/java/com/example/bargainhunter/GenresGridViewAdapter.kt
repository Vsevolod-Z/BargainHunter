package com.example.bargainhunter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.bargainhunter.models.Genres


class GenresGridViewAdapter(private val context: Context, private val fragment: GameListFragment) : BaseAdapter() {
    private var genres: MutableList<Genres> = mutableListOf()
    var selectedGenres: MutableList<Int> = mutableListOf()

    fun addGenres(genreList: MutableList<Genres>) {
        genres.addAll(genreList)
        Log.d("genres", "genres item count: $count")
    }

    override fun getCount(): Int {
        return genres.size
    }

    override fun getItem(position: Int): Any {
        return genres[position]
    }

    override fun getItemId(position: Int): Long {
        return genres[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: GenreViewHolder
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.genres_recycleview_item, parent, false)
            holder = GenreViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as GenreViewHolder
        }

        // Заполняем ViewHolder данными из списка
        holder.title.text = genres[position].description

        holder.card.setOnClickListener {
            if (genres[position].id in selectedGenres) {
                selectedGenres.removeAll(setOf(genres[position].id))
                holder.linearLayout.setBackgroundColor(context.getColor(R.color.TabDarkTheme))

            } else {
                selectedGenres.add(genres[position].id)
                holder.linearLayout.setBackgroundColor(context.getColor(R.color.dark_title_color))

            }
        }

        return view!!
    }

    class GenreViewHolder(itemView: View) {
        var title = itemView.findViewById<TextView>(R.id.tvGenreTitle)
        var card = itemView.findViewById<CardView>(R.id.genreCard)
        var linearLayout = itemView.findViewById<LinearLayout>(R.id.genreLayout)
    }
}