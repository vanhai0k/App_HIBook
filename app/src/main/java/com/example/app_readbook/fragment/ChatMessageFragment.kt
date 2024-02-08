package com.example.app_readbook.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_readbook.R
import com.example.app_readbook.adapter.ChatMessageAdapter
import com.example.app_readbook.`interface`.Users
import com.example.app_readbook.model.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatMessageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView: View
    private lateinit var adapter : ChatMessageAdapter

    private lateinit var iduser:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video, container, false)
        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatMessageAdapter(requireContext(), mutableListOf(), this)
        recyclerView.adapter = adapter
        getData()
        return rootView
    }

    private fun getData() {
        val apiService = Users.retrofit.create(Users::class.java)

// Gọi phương thức API getMessage với tham số userId
        val call = apiService.getMessage(iduser)

// Thực hiện cuộc gọi mạng bất đồng bộ (trên một luồng khác để tránh chặn luồng chính)
        call.enqueue(object : Callback<List<MessageModel>> {
            override fun onResponse(call: Call<List<MessageModel>>, response: Response<List<MessageModel>>) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    // Xử lý dữ liệu nhận được ở đây
                    if (messages != null) {
                        adapter.updateData(messages)
                    }
                } else {
                    Toast.makeText(context,"fail roi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MessageModel>>, t: Throwable) {
                // Xử lý trường hợp lỗi
                Toast.makeText(context,"fail:"+ t, Toast.LENGTH_SHORT).show()
                Log.d("loiiii", "onFailure: $t")
            }
        })

    }
    override fun onResume() {
        super.onResume()
        getData()
    }

}