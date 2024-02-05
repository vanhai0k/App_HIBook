package com.example.app_readbook.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.Image_Big
import com.example.app_readbook.R
import com.example.app_readbook.fragment_user.ImageFragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.model.ReadBook


class ImageAdapter (private val context: Context, var list: MutableList<ReadBook>, private val callback: ImageFragment): RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    interface AdapterCallback {
        fun onLikeClicked(postId: String)
    }


    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        var imagePost = v.findViewById<ImageView>(R.id.imagePost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.iteam_image,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posts = list[position]
        Glide.with(context).load(Link.url_mage + posts.image).into(holder.imagePost)

        holder.imagePost.setOnClickListener {
            val intent = Intent(holder.itemView.context, Image_Big::class.java)
            val buldle = Bundle()
            intent.putExtra("id", posts._id)
            intent.putExtra("imagepost", posts.image)
            intent.putExtras(buldle)
            holder.itemView.context.startActivity(intent)
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