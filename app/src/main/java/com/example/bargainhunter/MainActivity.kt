package com.example.bargainhunter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity

import com.example.bargainhunter.databinding.ActivityMainBinding

import com.google.android.material.tabs.TabLayoutMediator

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter



class MainActivity : AppCompatActivity() {

    var adapter: FragmentPagerItemAdapter? = null
    var pageCount = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
      /*  val editor = sharedPref.edit()
        editor.clear()
        editor.apply()*/
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
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
                // Инициализация данных
                // ...
                SteamUser.getUserData(this)
                while (!SteamUser.isInitialized){}
                    handler.post {
                        binding.progressBarCard.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }

            }.start()
        }



    }
    fun off(){

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