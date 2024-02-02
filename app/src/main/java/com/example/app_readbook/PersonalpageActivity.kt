package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.app_readbook.`interface`.Link

class PersonalpageActivity : AppCompatActivity() {

    lateinit var overlayImageView: ImageView
    lateinit var back: ImageView
    lateinit var tvusername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalpage)

        overlayImageView = findViewById(R.id.overlayImageView)
        tvusername = findViewById(R.id.tvusername)
        back = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }
//        cach nhan
        val receBundle : Bundle? = intent.extras
        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            val name = receBundle.getString("username")
            val img = receBundle.getString("image")

            tvusername.setText(name)
            Glide.with(this).load(Link.url_mage + img).into(overlayImageView)

        }


    }
}