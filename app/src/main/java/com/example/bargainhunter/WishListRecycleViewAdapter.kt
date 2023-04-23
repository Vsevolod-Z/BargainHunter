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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import androidx.core.content.res.ResourcesCompat
import com.example.bargainhunter.models.App


class WishListRecycleViewAdapter(private  val con: Context) : RecyclerView.Adapter<WishListRecycleViewAdapter.WishListViewHolder>(){

        lateinit var  wishList: MutableList<App>

    val context:Context

    init{
        context = con
        wishList= SteamUser.userData.apps    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        // Создаем ViewHolder для элемента списка
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_recycleview_item, parent, false)

        return WishListViewHolder(view)
    }

    override fun getItemCount() = wishList.size

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        // Заполняем ViewHolder данными из списка
        GameDataPreparer.imageDownloadAndSet(holder.img,holder.title,holder.bottomPanelLayout,wishList[position],context)
        holder.title.text = wishList[position].steamAppData.name
        GameDataPreparer.calculateRating(  wishList[position],holder.rating,holder.likeImage,context)
        GameDataPreparer.sortPrices(wishList[position])
        GameDataPreparer.fillGameData(wishList[position],holder.discount,holder.discountLayout,holder.initialPrice,holder.finalPrice,context)

    }


    class WishListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img = itemView.findViewById<ImageView>(R.id.icon)
        var title = itemView.findViewById<TextView>(R.id.tvTitle)
        var discount = itemView.findViewById<TextView>(R.id.tvDiscount)
        var rating = itemView.findViewById<TextView>(R.id.tvRating)
        var finalPrice = itemView.findViewById<TextView>(R.id.tvFinalPrice)
        var initialPrice = itemView.findViewById<TextView>(R.id.tvInitialPrice)
        var discountLayout = itemView.findViewById<ConstraintLayout>(R.id.dicountComtraintLayout)
        var priceLayout = itemView.findViewById<ConstraintLayout>(R.id.priceConstraintLayout)
        var bottomPanelLayout = itemView.findViewById<LinearLayout>(R.id.linerLayoutBottom)
        var likeImage = itemView.findViewById<ImageView>(R.id.likeImageView)
    }
}