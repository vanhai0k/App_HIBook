package com.example.app_readbook.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.ChatMessage_MainActivity
import com.example.app_readbook.R
import com.example.app_readbook.fragment.ChatMessageFragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.`interface`.Users
import com.example.app_readbook.model.MessageFriend
import com.example.app_readbook.model.MessageModel

class MessageAdapter (val context: Context, val list: MutableList<MessageFriend>,
                      private val callback: ChatMessage_MainActivity
): RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    fun updateData(newData: List<MessageFriend>?) {
        list.clear()
        newData?.let {
            list.addAll(it)
        }
        notifyDataSetChanged()
    }
    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val content = v.findViewById<TextView>(R.id.content)
        val timeMess = v.findViewById<TextView>(R.id.timeMess)
        val image = v.findViewById<ImageView>(R.id.imageFriend)
        val linnerMessage = v.findViewById<LinearLayout>(R.id.linnerMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_message, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.content.text = currentItem.message
        Glide.with(context).load(Link.url_mage + currentItem.user_id.image).into(holder.image)
        holder.timeMess.text = currentItem.timestamp

        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser", null).toString()

        val iduserMess = currentItem.user_id._id
        if (iduser == iduserMess) { // Use == for string comparison in Kotlin
            holder.image.visibility = View.GONE

            val layoutParams = holder.linnerMessage.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.END // Set gravity to move it to the right
            holder.linnerMessage.layoutParams = layoutParams

            holder.timeMess.gravity = Gravity.END // Set gravity for timestamp to align it properly
        } else {
            // In case the message is not sent by the current user, make sure the image and gravity are set to default
            holder.image.visibility = View.VISIBLE // Make sure the image is visible
            val layoutParams = holder.linnerMessage.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.START // Set gravity to move it to the left
            holder.linnerMessage.layoutParams = layoutParams

            holder.timeMess.gravity = Gravity.START // Set gravity for timestamp to align it properly
        }

    }
}