package com.example.bargainhunter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bargainhunter.models.App

class GameDataPreparer {
    companion object{
       lateinit var appPageApp : App
        fun openApp(context:Context,app:App){
            appPageApp = app
            val intent = Intent(context, AppPageActivity::class.java)

            context.startActivity(intent)
        }
        public fun calculateRating(
          app:App,
            tvRating: TextView,
            likeImage: ImageView,
            context:Context
        ) {
            var rating = ((app.steamAppData.app_review.query_summary.total_positive.toDouble() / app.steamAppData.app_review.query_summary.total_reviews.toDouble() ) * 100).toInt()
            if (rating!=0) {
                likeImage.setScaleY(1f)
                tvRating.text = context.getString(R.string.rating_percent, rating)
                Log.d("GameDataPreparer","rating: " + rating)
                if (rating >= 70) {
                    tvRating.setTextColor(ContextCompat.getColor(context, R.color.like_positive))
                    likeImage.setColorFilter(ContextCompat.getColor(context, R.color.like_positive))
                } else if (rating >= 50) {
                    tvRating.setTextColor(ContextCompat.getColor(context, R.color.like_neutral))
                   likeImage.setColorFilter(ContextCompat.getColor(context, R.color.like_neutral))
                } else {
                    likeImage.setScaleY(-1f)
                    tvRating.setTextColor(ContextCompat.getColor(context, R.color.like_negative))
                    likeImage.setColorFilter(ContextCompat.getColor(context, R.color.like_negative))
                }
            }

        }
        public fun calculateDiscount(last: Int, first: Int) : Int {
            return ((last - first).toFloat() / last * 100).toInt()
        }
        public  fun sortPrices(app: App): List<Int>{
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
        public fun imageDownloadAndSet(img:ImageView, app: App,context:Context){

            var bitmap: Bitmap
            try {

                Glide.with(context).asBitmap().load(app.steamAppData.header_image).into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ){
                        bitmap = resource
                        img.setImageBitmap(bitmap)
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
        public fun imageDownloadAndSet(img:ImageView,bottomPanelLayout:LinearLayout , app: App,context:Context){

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
                        img.setImageBitmap(bitmap)
                        color = bitmap.getPixel(0,bitmap.height-1)
                        color2 = bitmap.getPixel(bitmap.width-1,bitmap.height-1)
                        var colors = intArrayOf(color,color2)
                        var gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors)
                        bottomPanelLayout.background=gradient



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
        public fun imageDownloadAndSet(img:ImageView,title:TextView,bottomPanelLayout:LinearLayout , app: App,context:Context){

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
                        img.setImageBitmap(bitmap)
                        color = bitmap.getPixel(0,0)
                        color2 = bitmap.getPixel(bitmap.width-1,0)
                        var colors = intArrayOf(color,color2)
                        var gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors)
                        title.background= gradient
                        if (((color.let { ColorUtils.calculateLuminance(it) }
                                ?: 0.0) > 0.8) || ((color2.let { ColorUtils.calculateLuminance(it) }
                                ?: 0.0) > 0.8)) {
                            title.setTextColor(ContextCompat.getColor(context,R.color.dark_title_color))


                        }else {
                            title.setTextColor(ContextCompat.getColor(context,R.color.white))

                        }
                        color = bitmap.getPixel(0,bitmap.height-1)
                        color2 = bitmap.getPixel(bitmap.width-1,bitmap.height-1)
                        colors = intArrayOf(color,color2)
                        gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors)
                        bottomPanelLayout.background=gradient



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


        fun fillGameData(
            app: App,
            tvDiscount: TextView,
            discountLayout: ConstraintLayout,
            tvInitialPrice: TextView,
            tvFinalPrice: TextView,
            context: Context
        ) {
            if(app.steamAppData.price_overview.initial !=0) {
                var prices = sortPrices(app)
                var minPrice: Int
                var maxPrice: Int
                if (prices.last() > prices.first()) {
                    minPrice = prices.first()
                    maxPrice = prices.last()
                } else {
                    minPrice = prices.last()
                    maxPrice = prices.first()
                }


                tvFinalPrice.text = context.getString(R.string.final_price, minPrice)
                val discount: Int = calculateDiscount(maxPrice, minPrice)
                if (discount == 0 || discount < 0) {
                    tvDiscount.text = ""
                    discountLayout.setBackgroundResource(R.drawable.discount_background_transparent)
                } else {
                    tvDiscount.text = context.getString(R.string.discount_percent, discount)
                    discountLayout.setBackgroundResource(R.drawable.discount_background)
                }


                tvInitialPrice.text = context.getString(R.string.final_price, maxPrice)
                tvFinalPrice.typeface = ResourcesCompat.getFont(context, R.font.techno_race_italic)
                tvFinalPrice.textSize = 32f
            }else {
                tvInitialPrice.text=""
                tvFinalPrice.text="Бесплатно"
                tvFinalPrice.typeface = ResourcesCompat.getFont(context,R.font.new_zelek )
                tvFinalPrice.textSize = 20f
                tvDiscount.text=""
                discountLayout.setBackgroundResource(R.drawable.discount_background_transparent)
            }
        }
    }
}