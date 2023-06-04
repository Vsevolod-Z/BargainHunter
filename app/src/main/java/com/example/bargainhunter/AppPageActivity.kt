package com.example.bargainhunter


import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bargainhunter.models.App
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.Math.abs


class MonthAxisValueFormatter(private val months: List<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return months.getOrNull(value.toInt()) ?: value.toString()
    }
}
class AppPageActivity : AppCompatActivity() {

    private lateinit var app:App

    private  lateinit var rcvScreenshots:RecyclerView
    private  lateinit var rcvVideo:RecyclerView
    private  lateinit var rcvAchievements:RecyclerView
    private  lateinit var rcvReview: RecyclerView
    private  lateinit var rcvDLC: RecyclerView



    private lateinit var screenShotsAdapter: ScreenshotRecycleViewAdapter
    private lateinit var videoAdapter: VideoRecycleViewAdapter
    private lateinit var achievementsAdapter: AchievementsRecycleViewAdapter
    private lateinit var reviewAdapter: ReviewRecycleViewAdapter
    private lateinit var dlcAdapter: MainRecycleViewAdapter

    private lateinit var videoLayout: LinearLayout
    private lateinit var dlcLayout: LinearLayout
    private lateinit var achievementsLayout: LinearLayout

    private lateinit var steamPriceCard: CardView
    private lateinit var steamBuyPriceCard: CardView
    private lateinit var steamPayPriceCard: CardView
    private lateinit var gogPriceCard: CardView

    private lateinit var steamShareCard: CardView
    private lateinit var steamBuyShareCard: CardView
    private lateinit var steamPayShareCard: CardView
    private lateinit var gogShareCard: CardView

    private lateinit var tvDevelopers: TextView
    private lateinit var tvPublishers: TextView

    private lateinit var tvDate: TextView

    private lateinit var winAvailabilityImage: ImageView
    private lateinit var macAvailabilityImage: ImageView
    private lateinit var linuxAvailabilityImage: ImageView

    private lateinit var videoView: VideoView
    private lateinit var tvRequirments: TextView

    private lateinit var tvSteamPrice: TextView
    private lateinit var tvSteamBuyPrice: TextView
    private lateinit var tvSteamPayPrice: TextView
    private lateinit var tvGOGPrice: TextView

    private lateinit var tvSteamDiscount: TextView
    private lateinit var tvSteamBuyDiscount: TextView
    private lateinit var tvSteamPayDiscount: TextView
    private lateinit var tvGOGDiscount: TextView

    private lateinit var tvAppRating: TextView
    private lateinit var ratingProgressBar: ProgressBar
    private lateinit var likeImage: ImageView

    private lateinit var graphLayout:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_page)
        val scrlV = findViewById<ScrollView>(R.id.appScrollView)
        scrlV.post { scrlV.scrollTo(0, 0) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.main_color)))
        app = GameDataPreparer.appPageApp
        supportActionBar?.setTitle(app.steamAppData.name)
        requirementsInit()

        val icon = findViewById<ImageView>(R.id.iconImageView)
        GameDataPreparer.imageDownloadAndSet(icon, app,this.baseContext)

        dateInit()
        availabilityInit()
        developerAndPublisherInit()
        ratingInit()
        reviewInit()
        pricesInit()
        discountInit()

        app.playerPeeks?.let {
            playerPeeksInit()
        }
        app.steamAppData.achievements.highlighted?.let {
           achievementsInit()
        }
        app.steamAppData.dlc?.let {
            dlcInit()
        }
        initScreenShots()
        app.steamAppData.movies?.let {

            videoInit()
        }


    }
    fun dateInit(){
        tvDate = findViewById(R.id.tvDate)
        tvDate.text = app.steamAppData.release_date.date
    }
    fun availabilityInit(){
        winAvailabilityImage = findViewById(R.id.winAvailabilityImage)
        macAvailabilityImage = findViewById(R.id.macAvailabilityImage)
        linuxAvailabilityImage = findViewById(R.id.linuxAvailabilityImage)
        if(app.steamAppData.platforms.windows){
            winAvailabilityImage.setImageResource(R.drawable.check_outline)
        }
        if(app.steamAppData.platforms.mac){
        macAvailabilityImage.setImageResource(R.drawable.check_outline)
        }
        if(app.steamAppData.platforms.linux){
        linuxAvailabilityImage.setImageResource(R.drawable.check_outline)
        }
    }
    fun developerAndPublisherInit(){
        tvDevelopers = findViewById(R.id.tvDeveloper)
        tvPublishers = findViewById(R.id.tvPublisher)

        var strPublisher = ""
        var strDeveloper = ""
        for(dev in app.steamAppData.developers){
            strDeveloper += dev+"\n"
        }
        for(pub in app.steamAppData.publishers){
            strPublisher += pub+"\n"
        }
        tvPublishers.text = strPublisher
        tvDevelopers.text = strDeveloper
    }


    fun discountInit(){
        tvSteamDiscount = findViewById(R.id.tvSteamDiscount)
        tvSteamBuyDiscount = findViewById(R.id.tvSteamBuyDiscount)
        tvSteamPayDiscount = findViewById(R.id.tvSteamPayDiscount)
        tvGOGDiscount = findViewById(R.id.tvGOGDiscount)
        var sortPrices = GameDataPreparer.sortPrices(app)
        var maxPrice = app.steamAppData.price_overview.initial
        var discount : Int
        tvSteamDiscount.text = this.getString(R.string.discount_percent, GameDataPreparer.calculateDiscount(maxPrice,app.steamAppData.price_overview.final))
        if(app.steamBuyAppData.price.rub != "") {
             discount = GameDataPreparer.calculateDiscount(maxPrice,app.steamBuyAppData.price.rub.toInt())
            if(discount >0) {
                tvSteamBuyDiscount.text = this.getString(R.string.discount_percent, discount)
            }else{
                tvSteamBuyDiscount.text = this.getString(R.string.wrong_discount_percent, abs(discount))
                tvSteamBuyDiscount.setTextColor(getColor(R.color.like_negative))
            }
        }

        if(app.steamPayAppData.prices.rub != 0) {
             discount = GameDataPreparer.calculateDiscount(maxPrice,app.steamPayAppData.prices.rub)
            if(discount >0) {
                tvSteamPayDiscount.text = this.getString(R.string.discount_percent, GameDataPreparer.calculateDiscount(maxPrice,app.steamPayAppData.prices.rub))
            }else{
                tvSteamPayDiscount.text = this.getString(R.string.wrong_discount_percent, abs(discount))
                tvSteamPayDiscount.setTextColor(getColor(R.color.like_negative))
            }

        }
        if(app.gogAppData.price.finalAmount != "") {
            discount = GameDataPreparer.calculateDiscount(maxPrice,app.gogAppData.price.finalAmount.toFloat().toInt())
            if(discount >0) {
                tvGOGDiscount.text = this.getString(R.string.discount_percent, GameDataPreparer.calculateDiscount(maxPrice,app.steamPayAppData.prices.rub))
            }else{
                tvGOGDiscount.text = this.getString(R.string.wrong_discount_percent, abs(discount))
                tvGOGDiscount.setTextColor(getColor(R.color.like_negative))
            }

        }


    }
    fun ratingInit(){
        tvAppRating = findViewById(R.id.tvAppRating)
        ratingProgressBar = findViewById(R.id.ratingProgressBar)
        likeImage = findViewById(R.id.likeImage)
        GameDataPreparer.calculateRating(app,tvAppRating,likeImage,ratingProgressBar,this)
    }
    fun pricesInit(){
        steamShareCard = findViewById(R.id.steamShareCard)

        tvSteamPrice = findViewById(R.id.tvSteamPrice)
        steamPriceCard = findViewById(R.id.steamPriceCard)



        steamPriceCard.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.steamAppData.url))
            startActivity(intent)
        }

        if(app.steamAppData.price_overview.final != 0){
            steamShareCard.setOnClickListener{
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, String.format("Найдено благодаря Bargain Hunter:\n %s : %dp : %s \n %s",app.steamAppData.name,app.steamAppData.price_overview.final,"Steam",app.steamAppData.url))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться")
                startActivity(shareIntent)
            }
            tvSteamPrice.text = this.getString(R.string.final_price, app.steamAppData.price_overview.final)
        }else{
            steamShareCard.setOnClickListener{
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, String.format("Найдено благодаря Bargain Hunter:\n %s : %s : %s \n %s",app.steamAppData.name,"Бесплатно","Steam",app.steamAppData.url))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться")
                startActivity(shareIntent)
            }
            tvSteamPrice.text = "FREE"
        }

        if(app.steamBuyAppData.price.rub != "") {
            steamBuyPriceCard = findViewById(R.id.steamBuyPriceCard)
            steamBuyShareCard = findViewById(R.id.steamBuyShareCard)

            steamBuyShareCard.setOnClickListener{
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, String.format("Найдено благодаря Bargain Hunter:\n %s : %sp : %s \n %s",app.steamAppData.name,app.steamBuyAppData.price.rub,"SteamBuy",app.steamBuyAppData.url))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться")
                startActivity(shareIntent)
            }
            steamBuyPriceCard.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.steamBuyAppData.url))
                startActivity(intent)
            }
            tvSteamBuyPrice = findViewById(R.id.tvSteamBuyPrice)
            tvSteamBuyPrice.text = this.getString(R.string.final_price, app.steamBuyAppData.price.rub.toInt())
        }

        if(app.steamPayAppData.prices.rub != 0) {
            steamPayPriceCard = findViewById(R.id.steamPayPriceCard)
            steamPayShareCard = findViewById(R.id.steamPayShareCard)

            steamPayShareCard.setOnClickListener{
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, String.format("Найдено благодаря Bargain Hunter:\n %s : %dp : %s \n %s",app.steamAppData.name,app.steamPayAppData.prices.rub,"SteamPay",app.steamPayAppData.url))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться")
                startActivity(shareIntent)
            }
            steamPayPriceCard.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.steamPayAppData.url))
                startActivity(intent)
            }
            tvSteamPayPrice = findViewById(R.id.tvSteamPayPrice)
            tvSteamPayPrice.text = this.getString(R.string.final_price, app.steamPayAppData.prices.rub)
        }
        if(app.gogAppData.price.baseAmount != "") {
            gogPriceCard = findViewById(R.id.gogPriceCard)
            gogShareCard = findViewById(R.id.gogShareCard)
            Log.d("price",""+(app.gogAppData.price.finalAmount.toFloat().toInt())::class.java.typeName)
            gogShareCard.setOnClickListener{
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, String.format("Найдено благодаря Bargain Hunter:\n %s : %dp : %s \n %s",app.steamAppData.name,app.gogAppData.price.finalAmount.toFloat().toInt(),"GOG",app.gogAppData.url))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Поделиться")
                startActivity(shareIntent)
            }
            gogPriceCard.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.gogAppData.url))
                startActivity(intent)
            }
            tvGOGPrice = findViewById(R.id.tvGOGPrice)
            tvGOGPrice.text = this.getString(R.string.final_price, app.gogAppData.price.finalAmount.toFloat().toInt())
        }

    }
    fun reviewInit() {
        rcvReview = findViewById(R.id.rcvReview)
        rcvReview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reviewAdapter = ReviewRecycleViewAdapter(this,app.steamAppData.app_review.reviews,rcvReview)
        rcvReview.adapter = reviewAdapter
    }
    fun dlcInit() {
        rcvDLC = findViewById(R.id.rcvDLC)
        dlcLayout = findViewById(R.id.dlcLayout)
        dlcLayout.visibility = View.VISIBLE
        rcvDLC.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ApiClient.findByIds(app.steamAppData.dlc).observe(this) { apps ->
            dlcAdapter = MainRecycleViewAdapter(this, apps as MutableList<App>)
            rcvDLC.adapter = dlcAdapter
        }
    }
    fun achievementsInit() {
        rcvAchievements = findViewById(R.id.rcvAchievements)
        achievementsLayout = findViewById(R.id.achievementsLayout)
        rcvAchievements.layoutManager = LinearLayoutManager(this)
        achievementsLayout.visibility= View.VISIBLE
        achievementsAdapter = AchievementsRecycleViewAdapter(this,app.steamAppData.achievements.highlighted)
        rcvAchievements.adapter = achievementsAdapter
    }
    fun requirementsInit(){
        tvRequirments = findViewById(R.id.tvRequirements)
        var resultStr = ""
        if(!app.steamAppData.pc_requirements.minimum.isNullOrEmpty()) {
            for (i in 1..app.steamAppData.pc_requirements.minimum.size - 1) {
                if (i % 2 == 0) {
                    resultStr += app.steamAppData.pc_requirements.minimum[i] + "\n"
                } else {
                    resultStr += app.steamAppData.pc_requirements.minimum[i] + "\t\t"
                }
            }
        }
        Log.d("requ","requ: " + resultStr)
        tvRequirments.text = resultStr
    }
    fun playerPeeksInit(){
        graphLayout = findViewById(R.id.graphLayout)
        graphLayout.visibility= View.VISIBLE
        val date = mutableListOf<String>()
        val entries = mutableListOf<Entry>()
        for(peek in app.playerPeeks.reversed()){
            date.add(peek.timePeriod)
            entries.add(Entry(date.indexOf(peek.timePeriod).toFloat(), peek.count.toFloat()))
        }
        val lineChart = findViewById<LineChart>(R.id.line_chart)

        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.textColor = Color.WHITE
        lineChart.xAxis.granularity = 1f

        lineChart.axisLeft.granularity = 100f

        val dataSet = LineDataSet(entries, "Максимальный онлайн")
        lineChart.description.text = "График онлайна"



        val color = ContextCompat.getColor(this,R.color.blue)
        dataSet.color = color
        dataSet.lineWidth = 1f
        dataSet.valueTextColor = Color.WHITE
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisRight.textColor = Color.WHITE
        lineChart.description.textColor = Color.WHITE
        val font = ResourcesCompat.getFont(this, R.font.blue_curve)
        lineChart.xAxis.typeface = font
        lineChart.xAxis.textSize = 10f

// Установить шрифт и размер текста для подписи оси Y
        lineChart.axisLeft.typeface = font
        lineChart.axisLeft.textSize = 10f

// Установить шрифт и размер текста для описания графика
        lineChart.description.typeface = font
        lineChart.description.textSize = 10f


        val lineData = LineData(dataSet)
        lineChart.data = lineData
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MonthAxisValueFormatter(date)
        lineChart.invalidate()
    }
    fun initScreenShots(){
        rcvScreenshots = findViewById(R.id.rcvScreenshot)
        rcvScreenshots.layoutManager = CenterZoomLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        screenShotsAdapter = ScreenshotRecycleViewAdapter(this,app.steamAppData.screenshots)
        rcvScreenshots.adapter = screenShotsAdapter
    }
    fun videoInit(){
        videoLayout = findViewById(R.id.videoLayout)
        rcvVideo = findViewById(R.id.rcvVideo)
        videoView = findViewById(R.id.videoView)
        videoLayout.visibility = View.VISIBLE
        val screenWidth = resources.displayMetrics.widthPixels
        val videoViewWidth = screenWidth
        val videoViewHeight = (screenWidth * 9 / 16) // соотношение 16:9
        val layoutParams = videoView.layoutParams
        layoutParams.width = videoViewWidth
        layoutParams.height = videoViewHeight
        videoView.layoutParams = layoutParams
        videoAdapter = VideoRecycleViewAdapter(this,app.steamAppData.movies,videoView)
        rcvVideo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvVideo.adapter = videoAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onBackPressed() {
        super.finish()
    }
}