package com.example.app_readbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.R
import com.example.app_readbook.fragment.FriendFragment
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.model.ReadBook
import com.example.app_readbook.model.SendFriend
import com.example.app_readbook.model.User

class SendfriendAdapter(val context: Context, val list: MutableList<User>,
                        private val callback: FriendFragment
                            ): RecyclerView.Adapter<SendfriendAdapter.ViewHolder>(){
    fun updateData(newData: List<User>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val username = v.findViewById<TextView>(R.id.username)
        val image = v.findViewById<ImageView>(R.id.image)
        val delete = v.findViewById<LinearLayout>(R.id.delete)
        val confirm = v.findViewById<LinearLayout>(R.id.confirm)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_addfriend, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.sumBy { it.request?.size ?: 0 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val sendfr = list[position]
//        val send = sendfr.request
//
//
//        Log.d("Debug", "sendfr: $sendfr")
//        Log.d("Debug", "sendfr.request: ${sendfr.request}")
//        val usernameList: List<String>? = sendfr?.request?.mapNotNull { sendFriends ->
//            when (val user_id = sendFriends.user_id) {
//                is Map<*, *> -> user_id["username"]?.toString()
//                else -> null // Handle other cases or set a default value
//            }
//
//        }
//        holder.username.text = if (usernameList.isNullOrEmpty()) "No Usernames" else usernameList.joinToString(", ")


        var userIndex = 0
        var friendRequestIndex = position
        for (user in list) {
            val requestSize = user.request?.size ?: 0
            if (friendRequestIndex < requestSize) {
                // Display the friend request
                val friendRequest = user.request?.get(friendRequestIndex)
                val username = when (val user_id = friendRequest?.user_id) {
                    is Map<*, *> -> user_id["username"]?.toString()
                    else -> null // Handle other cases or set a default value
                }
                val imageUser = when (val user_id = friendRequest?.user_id) {
                    is Map<*, *> -> user_id["image"]?.toString()
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