package com.eric.phoneauction.detailChatFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemDetailChatBinding
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_detail_chat.view.*
import kotlinx.android.synthetic.main.item_detail_chat.view.image_chat_sender

class DetailChatAdapter(val viewModel: DetailChatViewModel) :
    androidx.recyclerview.widget.ListAdapter<Message, DetailChatAdapter.DetailChatViewHolder>(
        DiffCallback
    ) {

    class DetailChatViewHolder(private var binding: ItemDetailChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            message: Message, viewModel: DetailChatViewModel
        ) {
            binding.message = message
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailChatViewHolder {
        return DetailChatViewHolder(
            ItemDetailChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailChatViewHolder, position: Int) {
        val message = getItem(position)
        if (message.id == UserManager.userId) {
            holder.itemView.textview_chat_received.visibility = View.VISIBLE
            holder.itemView.text_chat_receivedTime.visibility = View.VISIBLE
            holder.itemView.text_chat_sentTime.visibility = View.GONE
            holder.itemView.textview_chat_sent.visibility = View.GONE
            holder.itemView.image_chat_sender.visibility = View.GONE

        } else {
            holder.itemView.textview_chat_received.visibility = View.GONE
            holder.itemView.text_chat_receivedTime.visibility = View.GONE
            holder.itemView.text_chat_sentTime.visibility = View.VISIBLE
            holder.itemView.textview_chat_sent.visibility = View.VISIBLE
            holder.itemView.image_chat_sender.visibility = View.VISIBLE
        }
        holder.bind(message, viewModel)
    }
}