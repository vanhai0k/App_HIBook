package com.example.app_readbook.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import com.example.app_readbook.Comment_Activity
import com.example.app_readbook.R
import com.example.app_readbook.fragment.NotificationFragment
import com.example.app_readbook.fragment_user.NouvellesFragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Readbook
import com.example.app_readbook.model.LikePosts
import com.example.app_readbook.model.ReadBook
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationAdapter (private val context: Context, var list: MutableList<ReadBook>, private val callback: NotificationFragment): RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        var image = v.findViewById<ImageView>(R.id.imageUserNotifi)
        var usernameNotifi = v.findViewById<TextView>(R.id.usernameNotifi)
        var contentNol = v.findViewById<TextView>(R.id.contentNol)
        var datetime = v.findViewById<TextView>(R.id.datetime)
        var linnerColor = v.findViewById<LinearLayout>(R.id.linnerColor)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_notification,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size

    }
    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val noitifi = list[position]
        val notifications = noitifi.notification


        if (!notifications.isNullOrEmpty()) {
            (holder.itemView as ViewGroup).removeAllViews()
            for (notification in notifications) {
                val user = notification.user_id
                val username = user?.username
                val imageUrl = user?.image

                val notificationView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.iteam_notification, holder.itemView as ViewGroup, false)

                val notificationtViewHolder = ViewHolder(notificationView)
                notificationtViewHolder.usernameNotifi.text = username

                // Kiểm tra xem imageUrl có giá trị không trước khi tải ảnh
                imageUrl?.let {
                    Glide.with(notificationtViewHolder.itemView.context).load(Link.url_mage + it).into(notificationtViewHolder.image)
                }
                notificationtViewHolder.datetime.text = notification.datetime
                notificationtViewHolder.contentNol.setText(noitifi.content)

                var color = notification.statusSend
                val customColor = Color.parseColor("#e7f3ff")
                if (color.equals("unread")){
                    notificationtViewHolder.linnerColor.setBackgroundColor(customColor)
                }else{
                    notificationtViewHolder.linnerColor.setBackgroundColor(Color.WHITE)

                }
                val id = noitifi._id
                val idnotification = notification._id
                val sharedPreferences = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
                val iduser_Login = sharedPreferences.getString("iduser", null)
                val likeList: List<LikePosts>? = noitifi.like ?: emptyList()
// Retrieve the currently logged-in user's ID from SharedPreferences
// Check if the currently logged-in user has liked the post
                val isUserLiked: Boolean = likeList?.any { it.user_id?._id == iduser_Login } ?: false
                holder.itemView.setOnClickListener {
                    val retrofit = Readbook.retrofit.create(Readbook::class.java)
                    val call = retrofit.updateNotificationStatus(id,idnotification)
                    call.enqueue(object : Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                // Xử lý khi API thành công
                                val intent = Intent(context,Comment_Activity::class.java)

                                val buldle = Bundle()
                                intent.putExtra("id", noitifi._id)
                                intent.putExtra("username", noitifi.userID?.username)
                                intent.putExtra("image", noitifi.userID?.image)
                                intent.putExtra("user_idpost", noitifi.userID?._id)
                                intent.putExtra("imagepost", noitifi.image)
                                intent.putExtra("datepost", noitifi.datepost)
                                intent.putExtra("content", noitifi.content)
                                intent.putExtra("isUserLiked", isUserLiked)
                                intent.putExtra("idpost", noitifi._id)
                                intent.putExtras(buldle)

                                context.startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(context,"Fail", Toast.LENGTH_SHORT).show()
                        }

                    })

                }


                (holder.itemView as ViewGroup).addView(notificationView)
            }
        }



    }
    fun updateData() {
        notifyDataSetChanged()
    }
    fun updateData(newList: List<ReadBook>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}