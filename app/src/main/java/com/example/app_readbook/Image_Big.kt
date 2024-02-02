package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.example.app_readbook.`interface`.Link

class Image_Big : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_big)

        var imagePost = findViewById<ImageView>(R.id.imagePost)
        var imageclose = findViewById<RelativeLayout>(R.id.imageclose)

        imageclose.setOnClickListener {
            onBackPressed()
        }
        val receBundle : Bundle? = intent.extras
        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            val imagepost = receBundle.getString("imagepost")

            Glide.with(this).load(Link.url_mage + imagepost).into(imagePost)

        }

    }
}