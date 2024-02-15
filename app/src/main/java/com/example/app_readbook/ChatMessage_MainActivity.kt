package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.adapter.ChatMessageAdapter
import com.example.app_readbook.adapter.MessageAdapter
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Users
import com.example.app_readbook.model.MessageFriend
import com.example.app_readbook.model.MessageModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ChatMessage_MainActivity : AppCompatActivity() {

    lateinit var usernamefiend: TextView
    lateinit var imageFriend:ImageView
    lateinit var imageback:ImageView
    lateinit var recyclerView:RecyclerView
    lateinit var adapter: MessageAdapter
    lateinit var idfriendMess:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message_main)

        imageback = findViewById(R.id.imageback)
        imageFriend = findViewById(R.id.imageFriend)
        usernamefiend = findViewById(R.id.usernamefiend)
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, mutableListOf(), this)
        recyclerView.adapter = adapter


        imageback.setOnClickListener {
            onBackPressed()
        }

        val receBundle : Bundle? = intent.extras
        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            val imagefriendMess = receBundle.getString("imagefriendMess")
            val namefriendMess = receBundle.getString("namefriendMess")
            idfriendMess = receBundle.getString("idfriendMess").toString()

            usernamefiend.setText(namefriendMess)
            Glide.with(this).load(Link.url_mage + imagefriendMess).into(imageFriend)

        }

        val sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()

        getData()
    }
    private lateinit var iduser:String
    private fun getData() {
        val apiService = Users.retrofit.create(Users::class.java)

// Gọi phương thức API getMessage với tham số userId
        val call = apiService.getChatMessageFriend(idfriendMess)
        call.enqueue(object : Callback<List<MessageModel>> {
            override fun onResponse(call: Call<List<MessageModel>>, response: Response<List<MessageModel>>) {
                if (response.isSuccessful) {
                    val messageModels = response.body()
                    messageModels?.let {
                        val messageFriends =
                            it.flatMap { messageModel -> messageModel.toMessageFriends() }
                        adapter.updateData(messageFriends)
                    }
                }else {
                    Toast.makeText(this@ChatMessage_MainActivity,"faillll", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MessageModel>>, t: Throwable) {
                Toast.makeText(this@ChatMessage_MainActivity,"fail:"+ t, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun MessageModel.toMessageFriends(): List<MessageFriend> {
        val messageFriends = mutableListOf<MessageFriend>()
        this.messages.forEach { message ->
            val messageFriend = MessageFriend(
                _id = message._id,
                user_id = message.user_id,
                message = message.message,
                timestamp = message.timestamp
            )
            messageFriends.add(messageFriend)
        }
        return messageFriends
    }

}