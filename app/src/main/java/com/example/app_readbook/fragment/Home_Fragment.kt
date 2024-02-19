package com.example.app_readbook.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.PersonalpageActivity
import com.example.app_readbook.R
import com.example.app_readbook.adapter.PostsAdapter
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Readbook
import com.example.app_readbook.model.ReadBook
import retrofit2.Call
import retrofit2.Response

class Home_Fragment : Fragment() {
    private lateinit var rootView: View
    lateinit var recyclerviewHome : RecyclerView
    lateinit var adapter: PostsAdapter
    lateinit var imageDD:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_, container, false)

        recyclerviewHome = rootView.findViewById(R.id.recyclerviewHome)
        imageDD = rootView.findViewById(R.id.imageDD)
        recyclerviewHome.layoutManager = LinearLayoutManager(context)


        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        val imageU = sharedPreferences?.getString("image", null)
        val iduser = sharedPreferences?.getString("iduser", null)
        val username = sharedPreferences?.getString("username", null)

        context?.let { Glide.with(it).load(Link.url_mage + imageU).into(imageDD) }

        imageDD.setOnClickListener {
            val intent = Intent(context, PersonalpageActivity::class.java)

            // Nếu bạn muốn truyền dữ liệu từ Activity cũ sang Activity mới, bạn có thể sử dụng putExtra
            // intent.putExtra("key", value)
            val buldle = Bundle()
            intent.putExtra("id", iduser)
            intent.putExtra("username", username)
            intent.putExtra("image", imageU)
            intent.putExtra("user_idpost", iduser)


            intent.putExtras(buldle)
            // Bắt đầu Activity mới
            val sharedPreferences = context?.getSharedPreferences("iduserNouls", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putString("user_idpost", iduser)

            editor?.apply()

            context?.startActivity(intent)
        }

        // Khởi tạo Adapter và truyền Interface vào
        adapter = PostsAdapter(requireContext(), mutableListOf(), this)
        recyclerviewHome.adapter = adapter

        getData()
        return rootView
    }

    private fun getData() {
        val retrofit = Readbook.retrofit.create(Readbook::class.java)
        val call = retrofit.getReadbook()
        call.enqueue(object : retrofit2.Callback<List<ReadBook>> {
            override fun onResponse(
                call: Call<List<ReadBook>>,
                response: Response<List<ReadBook>>
            ) {
                val data = response.body() ?: emptyList()

                // Cập nhật dữ liệu mới vào Adapter
                adapter.updateData(data)
            }

            override fun onFailure(call: Call<List<ReadBook>>, t: Throwable) {
                Log.d("loiii", "onFailure: " + t)
                Toast.makeText(context,"loi"+ t, Toast.LENGTH_SHORT).show()
            }

        })
    }
    // Override phương thức của Interface để xử lý sự kiện click vào like
    fun onLikeClicked(postId: String) {
        // Thực hiện các hành động khi click vào like, ví dụ: gọi hàm getData
        getData()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}