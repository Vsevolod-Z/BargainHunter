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


class MainRecycleViewAdapter(private  val con: Context,private val appList: MutableList<App>) : RecyclerView.Adapter<MainRecycleViewAdapter.MainViewHolder>(){
    val context:Context
    init{
    context = con
    }
     var isLoading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    fun addData(newDataList: List<App>) {
        appList.addAll(newDataList)
        notifyDataSetChanged()
    }

    fun clearData() {
        appList.clear()
        notifyDataSetChanged()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        // Создаем ViewHolder для элемента списка
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_recycleview_item, parent, false)

        return MainViewHolder(view)
    }

    override fun getItemCount() = appList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // Заполняем ViewHolder данными из списка
        imageDownloadAndSet(holder,appList[position])
        holder.title.text = appList[position].steamAppData.name
        val rating:Int = calculateRating(
            appList[position].steamAppData.app_review.query_summary.total_reviews,
            appList[position].steamAppData.app_review.query_summary.total_positive
        )
        if (rating!=0) {
            holder.rating.text = context.getString(R.string.rating_percent, rating)
        }
        if(appList[position].steamAppData.price_overview.initial !=0) {
            var prices = sortPrices(appList[position])
            var minPrice: Int
            var maxPrice: Int
            if (prices.last() > prices.first()){
                minPrice = prices.first()
                maxPrice = prices.last()
            }else{
                minPrice = prices.last()
                maxPrice = prices.first()
            }


            holder.finalPrice.text = context.getString(R.string.final_price,minPrice)
            val discount:Int = calculateDiscount(maxPrice, minPrice)
            if(discount == 0 || discount < 0){
                holder.discount.text=""
                holder.discountLayout.setBackgroundResource(R.drawable.discount_background_transparent)
            }else{
                holder.discount.text = context.getString(R.string.discount_percent,discount)
                holder.discountLayout.setBackgroundResource(R.drawable.discount_background)
            }


            holder.initialPrice.text = context.getString(R.string.final_price,maxPrice)
            holder.finalPrice.typeface = ResourcesCompat.getFont(context,R.font.techno_race_italic )
            holder.finalPrice.textSize = 32f

        }else {
            holder.initialPrice.text=""
            holder.finalPrice.text="Бесплатно"
            holder.finalPrice.typeface = ResourcesCompat.getFont(context,R.font.new_zelek )
            holder.finalPrice.textSize = 20f
            holder.discount.text=""
            holder.discountLayout.setBackgroundResource(R.drawable.discount_background_transparent)


        }

    }

    private fun calculateRating(initial: Int, current: Int) : Int {
        return ((current.toDouble() / initial.toDouble()) * 100).toInt()
    }
    private fun calculateDiscount(last: Int, first: Int) : Int {
        return ((last - first).toFloat() / last * 100).toInt()
    }
    private  fun sortPrices(app: App): List<Int>{
        var steamPrice = app.steamAppData.price_overview.final
        var prices = mutableListOf<Int>(steamPrice)
        if (app.steamPayAppData.prices.rub == 0) {
            var payPrice = app.steamPayAppData.prices.rub
        } else {
            var payPrice = app.steamPayAppData.prices.rub
            prices.add(payPrice)
        }
        if (app.steamBuyAppData.price.rub.isNotEmpty()) {

            var buyPrice = app.steamBuyAppData.price.rub.toInt()
            prices.add(buyPrice)
        }
        if (app.gogAppData.price.finalAmount.isNotEmpty()) {
                    var gogPrice = app.gogAppData.price.finalAmount.toFloat() * 82
            prices.add(gogPrice.toInt())
        }


        prices.sorted()
        return prices
    }
    private fun imageDownloadAndSet( holder: MainRecycleViewAdapter.MainViewHolder , app: App){

        var bitmap: Bitmap
        var color: Int
        var color2: Int

        try {

            Glide.with(context).asBitmap().load(app.steamAppData.header_image).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ){
                    bitmap = resource
                    holder.img.setImageBitmap(bitmap)
                    color = bitmap.getPixel(0,0)
                    color2 = bitmap.getPixel(bitmap.width-1,0)
                    var colors = intArrayOf(color,color2)
                    var gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors)
                    holder.title.background= gradient
                    if ((color.let { ColorUtils.calculateLuminance(it) } ?: 0.0 > 0.8) || (color2.let { ColorUtils.calculateLuminance(it) } ?: 0.0 > 0.8)) {
                        // действия, если цвет слишком светлый
                        holder.title.setTextColor(ContextCompat.getColor(context,R.color.dark_title_color))


                    }else {
                        holder.title.setTextColor(ContextCompat.getColor(context,R.color.white))

                    }
                    color = bitmap.getPixel(0,bitmap.height-1)
                    color2 = bitmap.getPixel(bitmap.width-1,bitmap.height-1)
                    colors = intArrayOf(color,color2)
                    gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors)
                    holder.bottomPanelLayout.background=gradient



                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Очистка загрузки изображения
                }
            })
            Log.d("image","steamData"+app.steamAppData.header_image)
        } catch (ex: Exception) {
            Log.e("Exception", ex.toString())
            Log.d("image","exception "+ex.toString())
        }

    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.findViewById<ImageView>(R.id.icon)
        var title = itemView.findViewById<TextView>(R.id.tvTitle)
        var discount = itemView.findViewById<TextView>(R.id.tvDiscount)
        var rating = itemView.findViewById<TextView>(R.id.tvRating)
        var finalPrice = itemView.findViewById<TextView>(R.id.tvFinalPrice)
        var initialPrice = itemView.findViewById<TextView>(R.id.tvInitialPrice)
        var discountLayout = itemView.findViewById<ConstraintLayout>(R.id.dicountComtraintLayout)
        var priceLayout = itemView.findViewById<ConstraintLayout>(R.id.priceConstraintLayout)
        var bottomPanelLayout = itemView.findViewById<LinearLayout>(R.id.linerLayoutBottom)

    }
}