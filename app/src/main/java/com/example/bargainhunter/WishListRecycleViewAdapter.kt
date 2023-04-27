package com.example.bargainhunter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bargainhunter.models.App


class WishListRecycleViewAdapter(private var con: Context,var wishListFragment: WishListFragment) : RecyclerView.Adapter<WishListRecycleViewAdapter.WishListViewHolder>(){

    companion object{
        private var adapterInstance: WishListRecycleViewAdapter? = null
        lateinit var  wishList: MutableList<App>
        fun updateWishList(){
            if(SteamUser.userData.apps != null) {
                wishList = SteamUser.userData.apps
                adapterInstance?.notifyDataSetChanged()
            }
            else{
                wishList = mutableListOf()
                adapterInstance?.notifyDataSetChanged()
            }
        }
    }

    private lateinit var view: View

    val context:Context

    init{
        adapterInstance = this
        context = con
        if(SteamUser.userData.apps != null)
        {
        wishList= SteamUser.userData.apps
        }
        else{
            wishList = mutableListOf()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        // Создаем ViewHolder для элемента списка
        view = LayoutInflater.from(parent.context).inflate(R.layout.main_recycleview_item, parent, false)

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
        holder.appCard.setOnClickListener{
            GameDataPreparer.openApp(view.context)
        }
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
        var appCard = itemView.findViewById<CardView>(R.id.appCard)
    }
}