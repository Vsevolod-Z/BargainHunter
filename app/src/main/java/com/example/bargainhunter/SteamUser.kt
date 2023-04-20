package com.example.bargainhunter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bargainhunter.models.App
import com.example.bargainhunter.models.Genres
import com.example.bargainhunter.models.SteamUserData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class SteamUser {

    companion object {
        var loading: Boolean = true
        var isInitialized:Boolean = false
        lateinit var userData: SteamUserData
        lateinit var userID: String
        fun getUserData(context: Context){
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            userID = prefs.getString("userId", "").toString()
            val savedTimestamp = prefs.getLong("timestamp", -1)
            Log.d("steam","userID:$userID,savedTimeStamp:$savedTimestamp")
            val now = System.currentTimeMillis()
            if (savedTimestamp != -1L && now - savedTimestamp > 7 * 24 * 60 * 60 * 1000 || savedTimestamp == -1L) {
                // Время создания id больше 7 дней, удаляем его
                if(userID != "") {
                    prefs.edit().remove("userId").remove("timestamp").apply()
                    Toast.makeText(
                        context,
                        "Даннеы пользователя устерели , пожалуйста войдите в стим заново!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else {
                    Log.d("steam","getSteamData()")
                    getSteamData()
            }
        }
        private fun getSteamData(){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://109.254.9.58:8080/auth/steamdata?steamid=$userID")
                .build()
            Log.d("steam", "url:  http://109.254.9.58:8080/auth/steamdata?steamid=$userID")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    Log.d("steam", "responseString: ${responseString}")
                    val gson: Gson = GsonBuilder().create()
                    val data: SteamUserData? =
                        gson.fromJson(responseString, SteamUserData::class.java)
                    // Добавить новые данные в список адаптера
                    if (data != null) {
                        userData = data
                    }
                    Log.d("steam", "userData: ${userData}")
                    loading = false
                    loadWishListAppData()

                } catch (e: Exception) {
                    Log.e("steam", "Error loading steamUserData", e)
                }
            }
        }
        fun updateUserID(context: Context,userId:String){
            userID= userId
            val timestamp = System.currentTimeMillis()
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("userId", userID)
                .putLong("timestamp", timestamp)
                .apply()
            getSteamData()
        }
        fun loadWishListAppData(){
            val wishList = userData.wishlist.joinToString(",")
            val client = OkHttpClient()
            var request = Request.Builder()
                .url("http://109.254.9.58:8080/api/apps/findByIds?appids=${wishList}")
                .build()
            Log.d("steam", "loadWishListAppData url: " + request.url)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    Log.d("steam", "loadWishListAppData responseString: ${responseString}")
                    val gson: Gson = GsonBuilder().create()

                    try {
                       userData.apps = gson.fromJson(responseString, object : TypeToken<List<App>>() {}.type)
                        // Добавить новые данные в список адаптера
                    } catch (e: JsonSyntaxException) {

                        // handle exception
                    }
                    isInitialized = true

                } catch (e: Exception) {
                    Log.e("loadNextData", "Error loading next data", e)
                }
            }
        }

    }
}