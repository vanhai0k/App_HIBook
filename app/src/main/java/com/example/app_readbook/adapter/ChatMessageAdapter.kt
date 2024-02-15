package com.example.app_readbook.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_readbook.ChatMessage_MainActivity
import com.example.app_readbook.R
import com.example.app_readbook.fragment.ChatMessageFragment
import com.example.app_readbook.fragment.Home_Fragment
import com.example.app_readbook.`interface`.Link
import com.example.app_readbook.model.MessageModel
import com.example.app_readbook.model.User
import java.nio.file.attribute.AclEntry.Builder

class ChatMessageAdapter (val context: Context, val list: MutableList<MessageModel>,
                          private val callback: ChatMessageFragment
): RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>(){

    fun updateData(newData: List<MessageModel>?) {
        list.clear()
        newData?.let {
            list.addAll(it)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val username = v.findViewById<TextView>(R.id.usernameFriend)
        val image = v.findViewById<ImageView>(R.id.imageFriend)
        val time = v.findViewById<TextView>(R.id.time)
        val tinnhan = v.findViewById<TextView>(R.id.tinnhan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.iteam_chat_friend, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = list[position]

        val sharedPreferences = context?.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser", null).toString()

        var idUserRece = friend.receiver?._id
        if (iduser.equals(idUserRece)){
            holder.username.setText(friend.sender?.username)
            Glide.with(context).load(Link.url_mage + friend.sender?.image).into(holder.image)
        }else{
            holder.username.setText(friend.receiver?.username)
            Glide.with(context).load(Link.url_mage + friend.receiver?.image).into(holder.image)
        }

        val lastMessage = friend.messages.lastOrNull()
        val messageContent = lastMessage?.message ?: ""

        val truncatedMessage = if (messageContent.length > 30) {
            messageContent.substring(0, 30) + "..."
        } else {
            messageContent
        }

        val isCurrentUserSender = lastMessage?.user_id?._id == iduser

        val messageToShow = if (isCurrentUserSender) {
            "Bạn: $truncatedMessage"
        } else {
            truncatedMessage
        }

        holder.tinnhan.text = messageToShow



        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatMessage_MainActivity::class.java)

            val bundle = Bundle()
            if (iduser.equals(idUserRece)) {
                intent.putExtra("idfriendMess", friend.sender?._id)
                intent.putExtra("namefriendMess", friend.sender?.username)
                intent.putExtra("imagefriendMess", friend.sender?.image)
                intent.putExtras(bundle)
            }
            else{
                intent.putExtra("idfriendMess", friend.receiver?._id)
                intent.putExtra("namefriendMess", friend.receiver?.username)
                intent.putExtra("imagefriendMess", friend.receiver?.image)
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}