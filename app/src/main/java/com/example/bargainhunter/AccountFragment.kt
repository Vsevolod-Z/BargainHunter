package com.example.bargainhunter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
        steamCard.setOnClickListener {
           launchChromeCustomTabForAuth()
        }
    }
    private fun launchChromeCustomTabForAuth() {
        val authUrl = "https://steamcommunity.com/openid/login?" +
                "openid.return_to=$serverUrl/auth/steamdata" +
                "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select" +
                "&openid.mode=checkid_setup" +
                "&openid.ns=http://specs.openid.net/auth/2.0" +
                "&openid.realm=$serverUrl/auth/steamdata"

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        customTabsIntent.launchUrl(requireContext(), Uri.parse(authUrl))
        Thread {
            try {
                Log.e("steamemum", "try")
                val url = URL("$serverUrl/auth/steamdata")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()

                    var line: String? = null
                    while (run {
                            line = bufferedReader.readLine()
                            line
                        } != null) {
                        stringBuilder.append(line)
                    }
                    inputStream.close()

                    val json = stringBuilder.toString()
                    Log.e("steamemum", "json: "+ json)
                }
            }catch (e: Exception) {
                Log.e("steamemum", "Error loading steam", e)
            }
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