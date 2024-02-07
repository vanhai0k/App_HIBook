package com.example.app_readbook.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.Friend_MainActivity
import com.example.app_readbook.R
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.model.User

class FriendAdapter (val context: Context, val list: MutableList<User>,private val callback: Friend_MainActivity): RecyclerView.Adapter<FriendAdapter.ViewHolder>(){
    fun updateData(newData: List<User>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val username = v.findViewById<TextView>(R.id.usernames)
        val image = v.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_friend, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.sumBy { it.friends?.size ?: 0 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var userIndex = 0
        var friendRequestIndex = position


        for (user in list) {
            val requestSize = user.friends?.size ?: 0
            if (friendRequestIndex < requestSize) {
                // Display the friend request
                val friendRequest = user.friends?.get(friendRequestIndex)
                val username = when (val user_friend = friendRequest?.user_friend) {
                    is Map<*, *> -> user_friend["username"]?.toString()
                    else -> null // Handle other cases or set a default value
                }
                val imageUser = when (val user_friend = friendRequest?.user_friend) {
                    is Map<*, *> -> user_friend["image"]?.toString()
                    else -> null // Handle other cases or set a default value
                }
                holder.username.text = username ?: "No Username"





                Glide.with(context)
                    .load(Link.url_mage +imageUser ?: R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)

                    .into(holder.image)
                return
            } else {
                // Move to the next user's friend requests
                friendRequestIndex -= requestSize
                userIndex++
            }
        }
    }
}