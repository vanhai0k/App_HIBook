package com.example.app_readbook

import android.content.Context
import com.android.volley.Request
import org.json.JSONObject
import com.android.volley.Response
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.app_readbook.adapter.FriendAdapter
import com.example.app_readbook.adapter.SendfriendAdapter
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Friend_MainActivity : AppCompatActivity() {

    private lateinit var rootView: View
    lateinit var recyclerview : RecyclerView
    lateinit var adapter: FriendAdapter
    lateinit var iduser: String

    lateinit var back: ImageView
    lateinit var tvusername: TextView
    lateinit var countFriend: TextView
    lateinit var linnernull: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_main)

        val sharedPreferences = getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()
        var username = sharedPreferences?.getString("username", null).toString()

        recyclerview = findViewById(R.id.recyclerviewFriend)
        tvusername = findViewById(R.id.tvusername)
        linnernull = findViewById(R.id.linnernull)
        countFriend = findViewById(R.id.countFriend)

        tvusername.setText("Bạn bè của " + username)
        back = findViewById(R.id.back)

        back.setOnClickListener {
            onBackPressed()
        }

        recyclerview.layoutManager = LinearLayoutManager(baseContext)

        // Khởi tạo Adapter và truyền Interface vào
        adapter = FriendAdapter(baseContext, mutableListOf(), this)
        recyclerview.adapter = adapter

        getData()
        val url = Link.url + "getUsersSend/"+ iduser

        val queue = Volley.newRequestQueue(baseContext)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                val friendsCounts = response.getString("friendsCount")
                countFriend.setText(friendsCounts +" bạn bè")
                // Tiếp tục xử lý dữ liệu
            },
            { error ->
                // Xử lý khi có lỗi xảy ra
            }
        )

// Thêm yêu cầu vào hàng đợi
        queue.add(jsonObjectRequest)
    }
    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val user = withContext(Dispatchers.IO) {
                    Users.retrofit.create(Users::class.java).getUser(iduser)
                }
                if (user!=null) {
                    adapter.updateData(listOf(user))
                }else{
                    linnernull.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}