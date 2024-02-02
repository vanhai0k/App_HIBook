package com.example.app_readbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.Comment_Activity
import com.example.app_readbook.R
import com.example.app_readbook.`interface`.Link.Companion.url_mage
import com.example.app_readbook.model.CommentPosts
import com.example.app_readbook.model.ReadBook
import java.lang.StringBuilder

class CommentAdapter (var context: Context, var list: MutableList<ReadBook>, private val callback: Comment_Activity): RecyclerView.Adapter<CommentAdapter.ViewHolder>(){
    interface AdapterCallback {
        fun onLikeClicked(postId: String)
    }
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val name = v.findViewById<TextView>(R.id.nameuser)
        val image = v.findViewById<ImageView>(R.id.imageComment)
        val imageuser = v.findViewById<ImageView>(R.id.imageuser)
        val content = v.findViewById<TextView>(R.id.comment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_comment, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = list[position]
        val comments = post.comment

// Check if comments is not null and not empty
        if (!comments.isNullOrEmpty()) {
            (holder.itemView as ViewGroup).removeAllViews()
            for (comment in comments) {
                val username = comment.user_id?.username
                val imageUrl = url_mage + comment.user_id?.image
                val imageComment = url_mage + comment.image
                Log.d("img", "onBindViewHolder: "+comment.image)
                val content = comment.content ?: "Chưa có bình luận nào!!!"

                // Set values for each comment
                val commentView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.iteam_comment, holder.itemView as ViewGroup, false)

                val commentViewHolder = ViewHolder(commentView)
                commentViewHolder.name.text = username
                Glide.with(commentViewHolder.itemView.context).load(imageUrl).into(commentViewHolder.imageuser)

                if (comment.image.isNullOrEmpty()){
                    commentViewHolder.image.visibility = View.GONE
                }else{
                    commentViewHolder.image.visibility = View.VISIBLE
                    Glide.with(commentViewHolder.itemView.context).load(imageComment).into(commentViewHolder.image)
                }

                commentViewHolder.content.text = content

                // Add the commentView to the parent layout of the original holder
                (holder.itemView as ViewGroup).addView(commentView)
            }
        } else {
                    // Handle the case where there are no comments
                    holder.name.text = "No comments"
                    holder.imageuser.setImageResource(R.drawable.ic_launcher_foreground) // Set a default image
                    holder.content.text = "Chưa có bình luận nào!!!"
                }

    }
//    Chưa có bình luận nào!!!
//    ic_launcher_foreground
    fun updateData(newList: ReadBook) {
        // Cập nhật dữ liệu (ví dụ: list) với dữ liệu mới được truyền vào
        list.clear() // Xóa toàn bộ dữ liệu cũ
        list.addAll(listOf(newList)) // Thêm dữ liệu mới

        // Thông báo cho Adapter biết rằng dữ liệu đã thay đổi
        notifyDataSetChanged()
    }
}