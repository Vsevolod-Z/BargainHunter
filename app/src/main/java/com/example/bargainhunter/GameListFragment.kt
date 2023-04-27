package com.example.bargainhunter

import android.content.Context
import android.content.Intent
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
        ApiClient.loadGenres(genresGridViewAdapter)
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                    searchRecylerView.visibility = View.VISIBLE
                    searchBack.visibility = View.VISIBLE
                    ApiClient.searchApps(searchAdapter,query)
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



        return myView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun loadNextData() {
        val genres = genresGridViewAdapter.selectedGenres.joinToString(",")
        this.context?.let { ApiClient.loadPaginationData(adapter,pageCount,genres, it) }
        pageCount++
    }

}