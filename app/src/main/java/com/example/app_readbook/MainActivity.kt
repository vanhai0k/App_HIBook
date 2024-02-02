package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.app_readbook.fragment.FriendFragment
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.fragment.NotificationFragment
import com.example.app_readbook.fragment.Setting_Fragment
import com.example.app_readbook.fragment.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView: BottomNavigationView = findViewById(R.id.bottom_nav)

        val bundle = Bundle()
        val homeFragment = Home_Fragment()
        homeFragment.arguments = bundle

        val settingFragment = Setting_Fragment()
        settingFragment.arguments = bundle

        val friendFragment = FriendFragment()
        friendFragment.arguments = bundle

        val notificationFragment = NotificationFragment()
        notificationFragment.arguments = bundle

        val videoFragment = VideoFragment()
        videoFragment.arguments = bundle

        replaceFragment(homeFragment)

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_event -> replaceFragment(videoFragment)
                R.id.nav_eventme -> replaceFragment(friendFragment)
                R.id.nav_chat -> replaceFragment(notificationFragment)
                R.id.nav_setting -> replaceFragment(settingFragment)
            }
            true
        }

    }
    // Di chuyển hàm replaceFragment ra khỏi onCreate
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}