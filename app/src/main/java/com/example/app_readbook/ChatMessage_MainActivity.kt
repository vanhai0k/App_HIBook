package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.app_readbook.`interface`.Link

class ChatMessage_MainActivity : AppCompatActivity() {

    lateinit var usernamefiend: TextView
    lateinit var imageFriend:ImageView
    lateinit var imageback:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message_main)

        imageback = findViewById(R.id.imageback)
        imageFriend = findViewById(R.id.imageFriend)
        usernamefiend = findViewById(R.id.usernamefiend)

        imageback.setOnClickListener {
            onBackPressed()
        }

        val receBundle : Bundle? = intent.extras
        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            val imagefriendMess = receBundle.getString("imagefriendMess")
            val namefriendMess = receBundle.getString("namefriendMess")
            val idfriendMess = receBundle.getString("idfriendMess")

            usernamefiend.setText(namefriendMess)
            Glide.with(this).load(Link.url_mage + imagefriendMess).into(imageFriend)

        }
    }
}