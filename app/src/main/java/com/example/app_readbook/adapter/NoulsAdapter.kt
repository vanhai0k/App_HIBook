package com.example.app_readbook.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.Comment_Activity
import com.example.app_readbook.Image_Big
import com.example.app_readbook.PersonalpageActivity
import com.example.app_readbook.R
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.fragment_user.NouvellesFragment
import com.example.app_readbook.`interface`.Interact
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.model.LikePosts
import com.example.app_readbook.model.ReadBook
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoulsAdapter (private val context: Context, var list: MutableList<ReadBook>, private val callback: NouvellesFragment): RecyclerView.Adapter<NoulsAdapter.ViewHolder>(){

    interface AdapterCallback {
        fun onLikeClicked(postId: String)
    }


    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        var image = v.findViewById<ImageView>(R.id.image)
        var username = v.findViewById<TextView>(R.id.username)
        var imagePost = v.findViewById<ImageView>(R.id.imagePost)
        var imagelikeback = v.findViewById<ImageView>(R.id.imagelikeback)
        var content = v.findViewById<TextView>(R.id.content)
        var textlike = v.findViewById<TextView>(R.id.textlike)
        var time = v.findViewById<TextView>(R.id.time)
        var countLike = v.findViewById<TextView>(R.id.countLike)
        var countcomment = v.findViewById<TextView>(R.id.countcomment)
        var linlerLike = v.findViewById<LinearLayout>(R.id.linlerLike)
        var linnerCountComment = v.findViewById<LinearLayout>(R.id.linnerComment)
        var linnercomment = v.findViewById<LinearLayout>(R.id.linnercomment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.iteam_posts,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posts = list[position]
        holder.content.setText(posts.content)
        Glide.with(context).load(Link.url_mage + posts.image).into(holder.imagePost)
        holder.username.setText(posts.userID?.username?: "DefaultUsername")
        Glide.with(context).load(Link.url_mage + posts.userID?.image).into(holder.image)
        holder.time.setText(posts.datepost)

        val id_post: String = posts._id
        val likeList: List<LikePosts>? = posts.like ?: emptyList()

// Retrieve the currently logged-in user's ID from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        val iduser_Login = sharedPreferences.getString("iduser", null)

// Check if the currently logged-in user has liked the post
        val isUserLiked: Boolean = likeList?.any { it.user_id?._id == iduser_Login } ?: false

// Display the appropriate image and text color based on whether the user has liked the post
        if (isUserLiked) {
            Glide.with(context)
                .load(R.drawable.likecolor)
                .into(holder.imagelikeback)
            holder.textlike.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaty))
            val likeCount = (posts.likeCount ?: 0) - 1
            if (likeCount > 0) {
                holder.countLike.text = "Bạn, $likeCount người khác"
            } else {
                holder.countLike.text = "Bạn"
            }
        } else {
            Glide.with(context)
                .load(R.drawable.like)
                .into(holder.imagelikeback)
            holder.textlike.setTextColor(ContextCompat.getColor(context, R.color.colorxam))
            val likeCounts = (posts.likeCount ?: 0)
            holder.countLike.text = "$likeCounts"
        }

        holder.linnercomment.setOnClickListener {
            val intent = Intent(holder.itemView.context, Comment_Activity::class.java)
            val buldle = Bundle()
            intent.putExtra("id", posts._id)
            intent.putExtra("username", posts.userID?.username)
            intent.putExtra("image", posts.userID?.image)
            intent.putExtra("imagepost", posts.image)
            intent.putExtra("datepost", posts.datepost)
            intent.putExtra("content", posts.content)
            intent.putExtra("isUserLiked", isUserLiked)
            intent.putExtra("idpost", posts._id)
            intent.putExtras(buldle)
            holder.itemView.context.startActivity(intent)
        }
        holder.imagePost.setOnClickListener {
            val intent = Intent(holder.itemView.context, Image_Big::class.java)
            val buldle = Bundle()
            intent.putExtra("id", posts._id)
            intent.putExtra("imagepost", posts.image)
            intent.putExtras(buldle)
            holder.itemView.context.startActivity(intent)
        }
        holder.linlerLike.setOnClickListener{
            if (iduser_Login != null) {
                val likeRequest = Interact.LikeRequest(user_id = iduser_Login)
                val retofit = Interact.retrofit.create(Interact::class.java)
                val call = retofit.likeNols(id_post, likeRequest)
                call.enqueue(object : Callback<Interact.LikeRequest> {
                    override fun onResponse(
                        call: Call<Interact.LikeRequest>,
                        response: Response<Interact.LikeRequest>
                    ) {
                        if (response.isSuccessful) {
                            val position = holder.adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                callback.onLikeClicked(list[position]._id)
                                updateData()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Interact.LikeRequest>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
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