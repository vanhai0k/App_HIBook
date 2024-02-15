package com.example.app_readbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.app_readbook.`interface`.Interact
import com.example.app_readbook.`interface`.Link
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalpageActivity : AppCompatActivity() {

    lateinit var overlayImageView: ImageView
    lateinit var back: ImageView
    lateinit var tvusername: TextView
    lateinit var tvusername1: TextView
    lateinit var title: TextView
    lateinit var user_idpost: String
    lateinit var iduser_Login: String
    lateinit var id_userpost: String
    lateinit var linnerMe:LinearLayout
    lateinit var linnerUser:LinearLayout
    lateinit var addFriend:LinearLayout
    lateinit var linnerMessage:LinearLayout
    lateinit var name:String
    lateinit var img:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalpage)


        val sharedPreferences = getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser_Login = sharedPreferences.getString("iduser", null).toString()


        overlayImageView = findViewById(R.id.overlayImageView)
        tvusername = findViewById(R.id.tvusername)
        tvusername1 = findViewById(R.id.tvusername1)
        back = findViewById(R.id.back)
        title = findViewById(R.id.title)
        linnerMe = findViewById(R.id.linnerMe)
        linnerUser = findViewById(R.id.linnerUser)
        addFriend = findViewById(R.id.addFriend)
        linnerMessage = findViewById(R.id.linnerMessage)


        val viewPager: ViewPager2 = findViewById(R.id.viewPage)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapterV = ViewPagerAdapter(this)
        viewPager.adapter = adapterV

        // Set up the TabLayout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Set tab names if needed
            when (position) {
                0 -> tab.text = "Bài viết"
                1 -> tab.text = "Ảnh"
            }
        }.attach()



        back.setOnClickListener {
            onBackPressed()
        }

//        cach nhan
        val receBundle : Bundle? = intent.extras

        if (receBundle != null) {
            val id = receBundle.getString("id").toString()
            name = receBundle.getString("username").toString()
            img = receBundle.getString("image").toString()
            id_userpost = receBundle.getString("user_idpost").toString()

            if (iduser_Login.equals(id_userpost)){
                linnerMe.visibility = View.VISIBLE
                linnerUser.visibility = View.GONE
            }else{
                linnerMe.visibility = View.GONE
                linnerUser.visibility = View.VISIBLE
            }

            tvusername.setText(name)
            tvusername1.setText(name)
            Glide.with(this).load(Link.url_mage + img).into(overlayImageView)

        }
        linnerMessage.setOnClickListener {
            val intent = Intent(baseContext,ChatMessage_MainActivity::class.java)

            val bundle = Bundle()
            intent.putExtra("idfriendMess", id_userpost)
            intent.putExtra("namefriendMess", name)
            intent.putExtra("imagefriendMess", img)
            intent.putExtras(bundle)

            startActivity(intent)
        }

        callCheckFriend()
    }

    private fun callCheckFriend() {
        val url = Link.url + "getUserCheckFrend/"+ iduser_Login

        val queue = Volley.newRequestQueue(baseContext)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                val friendsArray = response.getJSONArray("friends")
                val userFriendIds = mutableListOf<String>()

                // Lặp qua mảng bạn bè để lấy thông tin
                for (i in 0 until friendsArray.length()) {
                    val friendObj = friendsArray.getJSONObject(i)
                    val userFriendId = friendObj.getString("user_friend")
                    userFriendIds.add(userFriendId)
                }
                val requestArray = response.getJSONArray("request")
                val userRequests = mutableListOf<String>()

                // Lặp qua mảng bạn bè để lấy thông tin
                for (i in 0 until requestArray.length()) {
                    val requestObj = requestArray.getJSONObject(i)
                    val userID_request = requestObj.getString("user_id")
                    userRequests.add(userID_request)
                }


                // Kiểm tra xem id_userpost có trong danh sách bạn bè hay không
                if (userFriendIds.contains(id_userpost)) {
                    title.setText("Bạn bè")
                }
                else if (userRequests.contains(id_userpost)){
                    title.setText("Hủy lời mời")
                    addFriend.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("thông báo")
                            .setMessage("Bạn có muốn hủy lời mời kết bạn không?")
                            .setPositiveButton("Đồng ý") { dialog, _ ->
                                // Thực hiện hành động khi người dùng đồng ý
                                dialog.dismiss() // Dismiss dialog trước khi thực hiện hành động khác

                            }
                            .setNegativeButton("Hủy bỏ") { dialog, _ ->
                                // Thực hiện hành động khi người dùng chọn hủy bỏ
                                dialog.dismiss() // Dismiss dialog
                            }
                            .show() // Hiển thị dialog
                     }
                }else{
                    title.setText("Thêm bạn bè")
                    addFriend.setOnClickListener {
                        addFriends()
                    }
                }
            },
            { error ->
                // Xử lý khi có lỗi xảy ra
            }
        )

// Thêm yêu cầu vào hàng đợi
        queue.add(jsonObjectRequest)
    }

    private fun addFriends() {
        val usersService = Interact.retrofit.create(Interact::class.java)
        val friendId = iduser_Login // ID của người bạn muốn thêm vào danh sách bạn bè
        val sendfriend = Interact.Sendfriend(friendId) // Tạo đối tượng Sendfriend

        // Gửi yêu cầu thêm bạn bè đến API
        id_userpost?.let { userId ->
            val call = usersService.sendfriend(userId, sendfriend)
            call.enqueue(object : Callback<Interact.Sendfriend> {
                override fun onResponse(
                    call: Call<Interact.Sendfriend>,
                    response: Response<Interact.Sendfriend>
                ) {
                    if (response.isSuccessful){
                        title.text = "Hủy lời mời"
                        Toast.makeText(baseContext,"Đã gửi yêu cầu kết bạn", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext,"Không thể gửi yêu cầu kết bạn", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Interact.Sendfriend>, t: Throwable) {
                    Toast.makeText(baseContext,"Lỗi khi gửi yêu cầu kết bạn", Toast.LENGTH_SHORT).show()
                    Log.d("send", "onFailure: $t")
                }
            })
        }
    }

}