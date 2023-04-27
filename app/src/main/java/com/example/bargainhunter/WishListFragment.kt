package com.example.bargainhunter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WishListFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WishListRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_wish_list, container, false)
        // Inflate the layout for this fragment
        recyclerView = myView.findViewById<RecyclerView>(R.id.wishlistRCV)
        Log.d("wishList","SteamUser.isInitialized: ${SteamUser.isInitialized}")
        if (SteamUser.isInitialized) {

            adapter = WishListRecycleViewAdapter(myView.context,this)
            recyclerView.layoutManager = LinearLayoutManager(myView.context)
            recyclerView.adapter = adapter
        }else{
            Toast.makeText(
                context,
                "Войдите в стим, либо измените настройки приватности",
                Toast.LENGTH_LONG
            ).show()
        }

        return myView
    }
    override fun onResume() {
        super.onResume()
        if (SteamUser.isInitialized && recyclerView.adapter == null) {

            adapter = WishListRecycleViewAdapter(myView.context, this)
            recyclerView.layoutManager = LinearLayoutManager(myView.context)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }






}