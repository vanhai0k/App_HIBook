package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.app_readbook.fragment.FriendFragment
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.fragment.NotificationFragment
import com.example.app_readbook.fragment.Setting_Fragment
import com.example.app_readbook.fragment.ChatMessageFragment
import com.example.app_readbook.`interface`.Link
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import retrofit2.Response
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var navigationView: BottomNavigationView
    lateinit var iduser:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         navigationView = findViewById(R.id.bottom_nav)

        val bundle = Bundle()
        val homeFragment = Home_Fragment()
        homeFragment.arguments = bundle

        val settingFragment = Setting_Fragment()
        settingFragment.arguments = bundle

        val friendFragment = FriendFragment()
        friendFragment.arguments = bundle

        val notificationFragment = NotificationFragment()
        notificationFragment.arguments = bundle

        val videoFragment = ChatMessageFragment()
        videoFragment.arguments = bundle

        replaceFragment(homeFragment)

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_eventme -> replaceFragment(friendFragment)
                R.id.nav_event -> replaceFragment(videoFragment)
                R.id.nav_chat -> replaceFragment(notificationFragment)
                R.id.nav_setting -> replaceFragment(settingFragment)
            }
            true
        }

        callCountNotification()

    }

    private fun callCountNotification() {
        val sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()
        val url = Link.url + "countNoti/$iduser"

        val queue = Volley.newRequestQueue(baseContext)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                val countNoti = response.getInt("totalNotifications")
                // Tiếp tục xử lý dữ liệu
                if (countNoti != 0) {
                    navigationView.getOrCreateBadge(R.id.nav_chat).number = countNoti
                } else {
                    // Nếu countNoti = 0, không hiển thị số trên badge
                    navigationView.removeBadge(R.id.nav_chat)
                }
            },
            { error ->
                // Xử lý khi có lỗi xảy ra
            }
        )
// Thêm yêu cầu vào hàng đợi
        queue.add(jsonObjectRequest)
    }

    // Di chuyển hàm replaceFragment ra khỏi onCreate
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        callCountNotification()
    }
}