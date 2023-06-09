package com.example.bargainhunter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

import com.example.bargainhunter.databinding.ActivityMainBinding

import com.google.android.material.tabs.TabLayoutMediator

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter



class MainActivity : AppCompatActivity() {
    var adapter: FragmentPagerItemAdapter? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler

    override fun onResume() {
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()
        handler = Handler(Looper.getMainLooper())
        if(sharedPref.getString("userId", "").toString() != "") {
            binding.progressBarCard.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            Thread {
                SteamUser.getUserData(this)
                while (!SteamUser.isInitialized){}
                    handler.post {
                        binding.progressBarCard.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
            }.start()
        }
    }

    private  fun initial(){
        binding.pager.adapter = PagerAdapter(this)
        TabLayoutMediator( binding.tabLayout,  binding.pager) {
            tab,pos ->
            when(pos){
               0-> {
                   tab.setIcon(R.drawable.outline_home_24)
               }
                1-> {
                    tab.setIcon(R.drawable.outline_diamond_24)
                }
                2-> {
                    tab.setIcon(R.drawable.outline_account_circle_24)
                }
        }
        }.attach()
    }

}