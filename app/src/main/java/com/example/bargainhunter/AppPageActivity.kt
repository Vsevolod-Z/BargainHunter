package com.example.bargainhunter

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.bargainhunter.models.App

class AppPageActivity : AppCompatActivity() {
    private lateinit var app:App
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.main_color)))

        app = GameDataPreparer.appPageApp
        supportActionBar?.setTitle(app.steamAppData.name)
        var icon = findViewById<ImageView>(R.id.iconImageView)
        GameDataPreparer.imageDownloadAndSet(icon, app,this.baseContext)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onBackPressed() {
        finish()
        // выполните здесь действия, которые вы хотите повторить при нажатии кнопки назад
        super.onBackPressed() // не забудьте вызвать супер метод в конце, чтобы закрыть Activity
    }
}