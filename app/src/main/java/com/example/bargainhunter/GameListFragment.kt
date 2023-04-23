package com.example.bargainhunter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameListFragment() : Fragment() {
    var pageCount = 1
    private lateinit var myView: View

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchRecylerView: RecyclerView


    private lateinit var genresGridView: GridView

    private lateinit var confirmCardView: CardView
    private lateinit var filterButton: ImageButton

    private lateinit var searchView: SearchView
    private lateinit var searchBack: ImageView



    private lateinit var genresGridViewAdapter: GenresGridViewAdapter
    lateinit var adapter : MainRecycleViewAdapter
    lateinit var searchAdapter : SearchRecycleViewAdapter


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    public fun ResetPageCount(pCount: Int){
        adapter.clearData()
        pageCount = pCount
        loadNextData()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_game_list, container, false)

        recyclerView = myView.findViewById<RecyclerView>(R.id.rcV)
        searchRecylerView = myView.findViewById<RecyclerView>(R.id.searchViewRCV)


        filterButton = myView.findViewById(R.id.filterButton)
        genresGridView = myView.findViewById(R.id.genresGridView)
        confirmCardView = myView.findViewById(R.id.genresConfirmCardView)
        searchView = myView.findViewById(R.id.searchView)
        searchBack = myView.findViewById<ImageView>(R.id.searchBackButton)


        genresGridViewAdapter = GenresGridViewAdapter(myView.context,this)
        searchAdapter = SearchRecycleViewAdapter(myView.context)

        genresGridView.adapter = genresGridViewAdapter


        adapter = MainRecycleViewAdapter(myView.context, mutableListOf())
        loadGenres(genresGridViewAdapter)
        recyclerView.adapter = adapter

        searchRecylerView.layoutManager = LinearLayoutManager(myView.context)
        searchRecylerView.adapter=searchAdapter

        recyclerView.layoutManager = LinearLayoutManager(myView.context)


        searchBack.setOnClickListener {
            // Здесь можно обработать нажатие на кнопку крестика
            // Например, очистить содержимое SearchView
            searchView.setQuery("", false)
            searchRecylerView.visibility = View.GONE
            searchBack.visibility = View.GONE
            searchView.clearFocus()
            recyclerView.visibility = View.VISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                    searchRecylerView.visibility = View.VISIBLE
                    searchBack.visibility = View.VISIBLE
                    searchApps(searchAdapter,query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
              return false
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!adapter.isLoading && adapter.itemCount >= 10 &&
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                    firstVisibleItemPosition >= 0
                ) {
                    // Показать индикатор загрузки
                    adapter.setIsLoading(true)
                    // Загрузить следующую порцию данных
                    loadNextData()
                }
            }
        })
        loadNextData( )

        filterButton.setOnClickListener{
            searchView.visibility = View.GONE
            filterButton.visibility = View.GONE
            recyclerView.visibility = View.GONE
            genresGridView.visibility = View.VISIBLE
            confirmCardView.visibility = View.VISIBLE
        }
        confirmCardView.setOnClickListener{
            ResetPageCount(1)
            searchView.visibility = View.VISIBLE
            filterButton.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            genresGridView.visibility = View.GONE
            confirmCardView.visibility = View.GONE

        }

        return myView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }


    }
    fun searchApps(adapter: SearchRecycleViewAdapter,query:String){
        val client = OkHttpClient()
        var request = Request.Builder()
            .url("http://109.254.9.58:8080/api/apps/findByTitle?title=$query")
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseString = response.body!!.string()
                Log.d("gameList", "searchApps: ${responseString}")
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
            .url("http://109.254.9.58:8080/api/apps/getGenres")
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseString = response.body!!.string()
                Log.d("genres", "responseString: ${responseString}")
                val gson: Gson = GsonBuilder().create()

                     var genres:MutableList<Genres>  = gson.fromJson(responseString, object : TypeToken<List<Genres>>() {}.type)
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
    fun loadNextData() {
        // Загрузить следующую порцию данных из API или из другого источника
        // ...
        val genres = genresGridViewAdapter.selectedGenres.joinToString(",")
        val client = OkHttpClient()
        var request = Request.Builder()
            .url("http://109.254.9.58:8080/api/apps/getAppsPage?pageNum=" + pageCount + "&pageSize=" + 10 + "&genres="+genres)
            .build()
        Log.d("appList", "pageCount: " + pageCount)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val responseString = response.body!!.string()
                Log.d("appList", "responseString: ${responseString}")
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
                    Log.e("appList", "Error gson.fromJson data", e)
                    // handle exception
                }

            } catch (e: Exception) {
                Log.e("appList", "Error loading next data", e)
            }
        }
        pageCount++
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String,context: Context) =
            GameListFragment( ).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}