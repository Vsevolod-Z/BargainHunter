package com.example.bargainhunter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bargainhunter.models.App
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.BufferedReader

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var myView: View
    private lateinit var webView: WebView
    private lateinit var yearsCard: CardView
    private lateinit var avatarCard: CardView
    private lateinit var lvlCard: CardView
    private lateinit var tvYears: TextView
    private lateinit var tvLVL: TextView
    private lateinit var nickName: TextView
    private lateinit var icon: ImageView
    private lateinit var closeButtonLayout: ConstraintLayout
    var serverUrl = "http://109.254.9.58:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        yearsCard = view.findViewById<CardView>(R.id.yearsCard)
        avatarCard = view.findViewById<CardView>(R.id.avatarCard)
        lvlCard = view.findViewById<CardView>(R.id.lvlCard)
        var steamCard = view.findViewById<CardView>(R.id.steamCard)
        var discordCard = view.findViewById<CardView>(R.id.discordCard)
        var vkCard = view.findViewById<CardView>(R.id.vkCard)
        var telegramCard = view.findViewById<CardView>(R.id.telegramCard)


        var closeButton = view.findViewById<Button>(R.id.webCloseButton)
        closeButtonLayout = view.findViewById<ConstraintLayout>(R.id.closeButtonLayout)
        closeButton.setOnClickListener{
            webView.loadUrl("about:blank")
            webView.visibility=View.GONE
            closeButtonLayout.visibility=View.GONE
        }
        tvYears = view.findViewById<TextView>(R.id.textViewYearsNum)
        tvLVL = view.findViewById<TextView>(R.id.textViewLvlNum)
        nickName = view.findViewById<TextView>(R.id.textViewNickName)
        icon = view.findViewById<ImageView>(R.id.steamAvatar)
        yearsCard.visibility = View.INVISIBLE
        lvlCard.visibility = View.INVISIBLE
        avatarCard.visibility = View.INVISIBLE
        webView = view.findViewById(R.id.webView)
        setUserData()



        steamCard.setOnClickListener {
            openWebView()
            closeButtonLayout.isVisible=true
            webView.isVisible = true

        }
    }
    private fun setUserData(){
        val prefs = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        if (prefs != null && prefs.getString("userId", "").toString() != "" ) {
            imageDownloadAndSet(icon)
            tvLVL.text=SteamUser.userData.player_level.toString()
            tvYears.text=SteamUser.userData.years_of_service.toString()
            nickName.text=SteamUser.userData.personaname
            yearsCard.visibility = View.VISIBLE
            lvlCard.visibility = View.VISIBLE
            avatarCard.visibility = View.VISIBLE
        }
    }
    private fun imageDownloadAndSet( icon:ImageView){

        var bitmap: Bitmap
        try {

            Glide.with(myView.context).asBitmap().load(SteamUser.userData.avatarfull).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ){
                    bitmap = resource
                    icon.setImageBitmap(bitmap)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Очистка загрузки изображения
                }
            })

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}