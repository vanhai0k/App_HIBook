package com.example.app_readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.app_readbook.`interface`.Link
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PersonalpageActivity : AppCompatActivity() {

    lateinit var overlayImageView: ImageView
    lateinit var back: ImageView
    lateinit var tvusername: TextView
    lateinit var user_idpost: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalpage)

        overlayImageView = findViewById(R.id.overlayImageView)
        tvusername = findViewById(R.id.tvusername)
        back = findViewById(R.id.back)


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
            val name = receBundle.getString("username")
            val img = receBundle.getString("image")

            tvusername.setText(name)
            Glide.with(this).load(Link.url_mage + img).into(overlayImageView)

        }


    }
}