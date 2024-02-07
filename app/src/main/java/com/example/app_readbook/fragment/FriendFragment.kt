package com.example.app_readbook.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_readbook.Friend_MainActivity
import com.example.app_readbook.R
import com.example.app_readbook.adapter.SendfriendAdapter
import com.example.app_readbook.`interface`.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendFragment : Fragment() {

    private lateinit var rootView: View
    lateinit var recyclerview : RecyclerView
    lateinit var adapter: SendfriendAdapter
    lateinit var iduser: String
    lateinit var linnerFriend: LinearLayout
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_friend, container, false)

        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()

        recyclerview = rootView.findViewById(R.id.recyclerview)
        linnerFriend = rootView.findViewById(R.id.linnerFriend)

        linnerFriend.setOnClickListener {
            val intent = Intent(rootView.context,Friend_MainActivity::class.java)

            startActivity(intent)
        }

        recyclerview.layoutManager = LinearLayoutManager(context)

        // Khởi tạo Adapter và truyền Interface vào
        adapter = SendfriendAdapter(requireContext(), mutableListOf(), this)
        recyclerview.adapter = adapter

        getData()

        return rootView
    }
    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val user = withContext(Dispatchers.IO) {
                    Users.retrofit.create(Users::class.java).getUser(iduser)
                }
                adapter.updateData(listOf(user))

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