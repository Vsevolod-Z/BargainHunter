package com.example.bargainhunter

import android.content.Context
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.bargainhunter.databinding.ActivityMainBinding

import com.google.android.material.tabs.TabLayoutMediator

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter



class MainActivity : AppCompatActivity() {

    var adapter: FragmentPagerItemAdapter? = null
    var pageCount = 1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        prefs.edit().remove("userId").remove("timestamp").apply()
        SteamUser.getUserData(this)
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()




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