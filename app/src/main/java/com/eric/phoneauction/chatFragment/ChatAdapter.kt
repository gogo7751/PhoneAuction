package com.eric.phoneauction.chatFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemChatBinding
import com.eric.phoneauction.util.Logger
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(val viewModel:ChatViewModel) :
    androidx.recyclerview.widget.ListAdapter<ChatRoom, ChatAdapter.DetailChatViewHolder>(
        DiffCallback
    ) {

    class DetailChatViewHolder(private var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            chatRoom: ChatRoom, viewModel: ChatViewModel
        ) {
            binding.chatRoom = chatRoom
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailChatViewHolder {
        return DetailChatViewHolder(
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailChatViewHolder, position: Int) {
        val chatRoom = getItem(position)
        if (UserManager.userId == chatRoom.senderId) {
            holder.itemView.text_chat_title.text = chatRoom.receiverName
            holder.itemView.image_chat_sender.visibility = View.GONE
        }else if (UserManager.userId == chatRoom.receiverId) {
            holder.itemView.text_chat_title.text = chatRoom.senderName
            holder.itemView.image_chat_receiver.visibility = View.GONE
        } else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams.height = 0
        }
        holder.bind(chatRoom, viewModel)
    }
}