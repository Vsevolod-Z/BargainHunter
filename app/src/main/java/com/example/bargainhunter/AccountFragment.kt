package com.example.bargainhunter

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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


        var yearsCard = view.findViewById<CardView>(R.id.yearsCard)
        var lvlCard = view.findViewById<CardView>(R.id.lvlCard)
        var steamCard = view.findViewById<CardView>(R.id.steamCard)
        var discordCard = view.findViewById<CardView>(R.id.discordCard)
        var vkCard = view.findViewById<CardView>(R.id.vkCard)
        var telegramCard = view.findViewById<CardView>(R.id.telegramCard)

        var tvYears = view.findViewById<TextView>(R.id.textViewYearsNum)
        var tvLVL = view.findViewById<TextView>(R.id.textViewLvlNum)
        var nickName = view.findViewById<TextView>(R.id.textViewNickName)
        var icon = view.findViewById<ImageView>(R.id.steamAvatar)

        webView = view.findViewById(R.id.webView)



        steamCard.setOnClickListener {
            openWebView()
            webView.isVisible = true

        }
    }
    private fun openWebView() {

        webView.clearCache(true)
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
                    webView.isVisible = false
                    webView.loadUrl("about:blank")
                    Log.d("steamemum","steamId " + steamId)
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

            webView.loadUrl(
                "https://steamcommunity.com/openid/login?openid.return_to=http://anime&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.mode=checkid_setup&openid.ns=http://specs.openid.net/auth/2.0&openid.realm=http://anime"
            )


    }
    class MyWebViewClient : WebViewClient() {


        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d("steamemum","url: "+ url)
            if (url != null) {
                if ( url.startsWith("https://steamcommunity.com") || url.startsWith("https://steamcommunity.com") ) {
                    view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    return true
                }
            }
            if (url?.startsWith("http://109.254.9.58:8080/auth/steamdata?openid.ns") == true) {
                // URL-адрес начинается с нужной строки, загружаем содержимое страницы
                val httpClient = DefaultHttpClient()
                val httpGet = HttpGet(url)
                val response = httpClient.execute(httpGet)
                val inputStream = response.entity.content

                // Извлекаем JSON из тела ответа
                val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
                val jsonObject = JSONObject(jsonString)


                Log.d("steamemum","jsonObject: "+ jsonObject)
                // Делаем что-то с полученным JSON

                // Возвращаем true, чтобы сообщить WebView, что мы обработали этот URL-адрес
                return true
            }

            // Возвращаем false, чтобы WebView обработала этот URL-адрес самостоятельно
            return false
        }

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