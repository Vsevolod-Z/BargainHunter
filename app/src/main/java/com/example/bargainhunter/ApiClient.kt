package com.example.bargainhunter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bargainhunter.models.App
import com.example.bargainhunter.models.Genres
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

class ApiClient {
    companion object{
        val pageSize = 10
        val serverUrl = "http://109.254.9.58:8080"
        val paginationQuery = "%s/api/apps/getAppsPage?pageNum=%d&pageSize=%d&genres=%s"
        val genresQuery = "${serverUrl}/api/apps/getGenres"
        val searchAppsQuery = "${serverUrl}/api/apps/findByTitle?title=%s"
        val findByIdsQuery = "${serverUrl}/api/apps/findByIds?appids=%s"
        lateinit var genres:MutableList<Genres>

        fun searchApps(adapter: SearchRecycleViewAdapter,query:String){
            val client = OkHttpClient()
            var url = String.format(searchAppsQuery,query)
            var request = Request.Builder()
                .url(url)
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    Log.d("ApiClient", "searchApps: ${responseString}")
                    val gson: Gson = GsonBuilder().create()

                    var apps:MutableList<App>  = gson.fromJson(responseString, object : TypeToken<List<App>>() {}.type)
                    // Добавить новые данные в список адаптера
                    withContext(Dispatchers.Main) {
                        adapter.updateAppList(apps)
                        adapter.notifyDataSetChanged()
                        // Скрыть индикатор загрузки
                    }


                } catch (e: Exception) {
                    Log.e("loadNextData", "Error loading next data", e)
                }
            }
        }
        fun loadGenres(adapter: GenresGridViewAdapter){
            val client = OkHttpClient()
            var request = Request.Builder()
                .url(genresQuery)
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    Log.d("ApiClient", "responseString: ${responseString}")
                    val gson: Gson = GsonBuilder().create()

                    genres = gson.fromJson(responseString, object : TypeToken<List<Genres>>() {}.type)
                    // Добавить новые данные в список адаптера
                    withContext(Dispatchers.Main) {
                        adapter.addGenres(genres)
                        adapter.notifyDataSetChanged()
                        // Скрыть индикатор загрузки

                    }


                } catch (e: Exception) {
                    Log.e("loadNextData", "Error loading next data", e)
                }
            }
        }
        fun loadPaginationData(adapter:MainRecycleViewAdapter,pageCount:Int,genres:String,context:Context){

            var url = String.format(paginationQuery,serverUrl,pageCount,pageSize,genres)
            Log.d("ApiClient", "url: " + url)
            val client = OkHttpClient()
            var request = Request.Builder()
                .url(url)
                .build()
            Log.d("ApiClient", "pageCount: " + pageCount)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    Log.d("ApiClient", "responseString: ${responseString}")
                    val gson: Gson = GsonBuilder().create()

                    try {
                        val appList: List<App> = gson.fromJson(responseString, object : TypeToken<List<App>>() {}.type)
                        // Добавить новые данные в список адаптера
                        withContext(Dispatchers.Main) {

                            adapter.addData(appList)
                            // Скрыть индикатор загрузки
                            adapter.setIsLoading(false)
                            adapter.notifyDataSetChanged()
                        }
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Error gson.fromJson data", e)
                        // handle exception
                    }

                } catch (e: Exception) {
                    Log.e("ApiClient", "Error loading next data", e)
                }
            }
        }
        fun findByIds(ids: List<Int>): LiveData<List<App>> {
            val resultLiveData = MutableLiveData<List<App>>()
            val client = OkHttpClient()
            val idsStr = ids.joinToString(",")
            val request = Request.Builder().url(String.format(findByIdsQuery,idsStr)).build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseString = response.body!!.string()
                    val gson: Gson = GsonBuilder().create()
                    val apps = gson.fromJson<List<App>>(responseString, object : TypeToken<List<App>>() {}.type)
                    resultLiveData.postValue(apps)
                } catch (e: Exception) {
                    Log.e("loadNextData", "Error loading next data", e)
                }
            }
            return resultLiveData
        }
    }
}