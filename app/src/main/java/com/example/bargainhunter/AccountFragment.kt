package com.example.bargainhunter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class AccountFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var webView: WebView
    private lateinit var avatarCard: CardView
    private lateinit var nickName: TextView
    private lateinit var tvYears: TextView
    private lateinit var tvLvL: TextView
    private lateinit var icon: ImageView
    private lateinit var closeButtonLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        avatarCard = view.findViewById<CardView>(R.id.avatarCard)

        var steamCard = view.findViewById<CardView>(R.id.steamCard)
        var discordCard = view.findViewById<CardView>(R.id.discordCard)
        var vkCard = view.findViewById<CardView>(R.id.vkCard)
        var telegramCard = view.findViewById<CardView>(R.id.telegramCard)

        tvYears = view.findViewById<TextView>(R.id.tvYears)
        tvLvL = view.findViewById<TextView>(R.id.tvLvL)

        var closeButton = view.findViewById<Button>(R.id.webCloseButton)
        closeButtonLayout = view.findViewById<ConstraintLayout>(R.id.closeButtonLayout)
        closeButton.setOnClickListener{
            webView.loadUrl("about:blank")
            webView.visibility=View.GONE
            closeButtonLayout.visibility=View.GONE
            avatarCard.visibility = View.VISIBLE
            tvLvL.visibility = View.VISIBLE
            tvYears.visibility = View.VISIBLE

        }

        nickName = view.findViewById<TextView>(R.id.textViewNickName)
        icon = view.findViewById<ImageView>(R.id.steamAvatar)

        avatarCard.visibility = View.INVISIBLE
        webView = view.findViewById(R.id.webView)
        setUserData()


        steamCard.setOnClickListener {
            openWebView()
            avatarCard.visibility = View.INVISIBLE
            tvLvL.visibility = View.INVISIBLE
            tvYears.visibility = View.INVISIBLE
            webView.bringToFront()
            closeButtonLayout.bringToFront()
            closeButtonLayout.isVisible=true
            webView.isVisible = true

        }
    }
    private fun setUserData(){
        val prefs = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        Log.d("account","userId"+ prefs!!.getString("userId", "").toString())
        if (prefs.getString("userId", "").toString() != "") {
            imageDownloadAndSet(icon)
            tvYears.text =  myView.context.getString(R.string.account_years,SteamUser.userData.years_of_service.toString())
            tvLvL.text =  myView.context.getString(R.string.account_lvl,SteamUser.userData.player_level.toString())
            nickName.text=SteamUser.userData.personaname
            avatarCard.visibility = View.VISIBLE
            tvYears.visibility = View.VISIBLE
            tvLvL.visibility = View.VISIBLE

        }
    }
    private fun imageDownloadAndSet( icon:ImageView){
        try {

            Glide.with(myView.context).asBitmap().load(SteamUser.userData.avatarfull).into(icon)

        } catch (ex: Exception) {
            Log.e("Exception", ex.toString())
        }

    }
    private fun openWebView() {

        webView.clearCache(true)
        webView.settings.setJavaScriptEnabled(true)
        webView.settings.setDomStorageEnabled(true)
        webView.settings.setLoadsImagesAutomatically(true)
        webView.settings.setLoadWithOverviewMode(true)
        webView.settings.setUseWideViewPort(true)
        webView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && webView.canGoBack()) {
                webView.goBack()
                return@OnKeyListener true
            }
            if(keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && !webView.canGoBack()){
                webView.visibility = View.GONE
            }

            false
        })
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                Log.d("steamemum","url " + url)
                if (url.startsWith("http://anime/?openid.ns",  true)) {
                    // Steam авторизация

                    val uri = Uri.parse(url)
                    val identity = uri.getQueryParameter("openid.identity")
                    val steamId = identity?.replace("https://steamcommunity.com/openid/id/", "")
                    webView.visibility = View.GONE
                    closeButtonLayout.visibility = View.GONE
                    webView.loadUrl("about:blank")
                    if (steamId != null) {
                        SteamUser.updateUserID(myView.context,steamId)
                        while (SteamUser.loading){

                        }
                        setUserData()
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

            webView.loadUrl(
                "https://steamcommunity.com/openid/login?openid.return_to=http://anime&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.mode=checkid_setup&openid.ns=http://specs.openid.net/auth/2.0&openid.realm=http://anime"
            )


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_account, container, false)

        return inflater.inflate(R.layout.fragment_account, container, false)
    }
}