package com.example.app_readbook.fragment_user

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_readbook.R
import com.example.app_readbook.adapter.ImageAdapter
import com.example.app_readbook.adapter.NoulsAdapter
import com.example.app_readbook.`interface`.Readbook
import com.example.app_readbook.model.ReadBook
import retrofit2.Call
import retrofit2.Response

class ImageFragment : Fragment() {
    private lateinit var rootView: View
    lateinit var recyclerviewHome : RecyclerView
    lateinit var adapter: ImageAdapter
    lateinit var iduser:String
    lateinit var user_idpost:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_image, container, false)

        val sharedPreferences = context?.getSharedPreferences("iduserNouls", Context.MODE_PRIVATE)
        user_idpost = sharedPreferences?.getString("user_idpost", null).toString()
        recyclerviewHome = rootView.findViewById(R.id.recyclerviewNonls)
        recyclerviewHome.layoutManager = GridLayoutManager(context,3)

        // Khởi tạo Adapter và truyền Interface vào
        adapter = ImageAdapter(requireContext(), mutableListOf(), this)
        recyclerviewHome.adapter = adapter

        getData()

        return rootView
    }
    private fun getData() {
        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()
        val retrofit = Readbook.retrofit.create(Readbook::class.java)
        val call = retrofit.getReadbookUser(user_idpost)
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