package com.example.app_readbook.adapter

import android.content.Context
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
import com.example.app_readbook.R
import com.example.app_readbook.fragment.FriendFragment
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.`interface`.Interact
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Users
import com.example.app_readbook.model.ReadBook
import com.example.app_readbook.model.SendFriend
import com.example.app_readbook.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    lateinit var iduser: String
    lateinit var id_userFriend: String
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sharedPreferences = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        iduser = sharedPreferences?.getString("iduser", null).toString()
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
                id_userFriend = when (val _id = friendRequest?.user_id) {
                    is Map<*, *> -> _id["_id"]?.toString().toString()
                    else -> null.toString() // Handle other cases or set a default value
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

                holder.confirm.setOnClickListener {
                    val usersService = Interact.retrofit.create(Interact::class.java)
                    var user_id = iduser
                    var status = "accepted"
                    val confirmFriend = Interact.ConfirmFriend(user_id,status)
                    val call = usersService.confirmFriend(id_userFriend,confirmFriend)
                    call.enqueue(object : Callback<Interact.ConfirmFriend>{
                        override fun onResponse(
                            call: Call<Interact.ConfirmFriend>,
                            response: Response<Interact.ConfirmFriend>
                        ) {
                            list.let {
                                if (position in 0 until it.size) { // Check if position is within bounds
                                    it.removeAt(position)
                                    notifyDataSetChanged()
                                    Toast.makeText(context, "Đã thành bạn bè", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.e("SendfriendAdapter", "Invalid position: $position, List size: ${it.size}")
                                }
                            }

                        }

                        override fun onFailure(call: Call<Interact.ConfirmFriend>, t: Throwable) {
                            Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
                            Log.d("send", "onFailure: $t")
                        }

                    })
                }
                holder.delete.setOnClickListener {
                    val usersService = Interact.retrofit.create(Interact::class.java)
                    var user_id = iduser
                    var status = "rejected"
                    val confirmFriend = Interact.ConfirmFriend(user_id,status)
                    val call = usersService.confirmFriend(id_userFriend,confirmFriend)
                    call.enqueue(object : Callback<Interact.ConfirmFriend>{
                        override fun onResponse(
                            call: Call<Interact.ConfirmFriend>,
                            response: Response<Interact.ConfirmFriend>
                        ) {
                            if (response.isSuccessful){
                                list.removeAt(position)
                                notifyDataSetChanged()
                                Toast.makeText(context,"Đã phàn bạn bè",Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Interact.ConfirmFriend>, t: Throwable) {
                            Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
                            Log.d("send", "onFailure: $t")
                        }

                    })
                }
                return
            } else {
                // Move to the next user's friend requests
                friendRequestIndex -= requestSize
                userIndex++
            }
        }
    }
}