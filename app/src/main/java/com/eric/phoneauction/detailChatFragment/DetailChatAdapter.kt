package com.eric.phoneauction.detailChatFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemDetailChatBinding

class DetailChatAdapter(val viewModel: DetailChatViewModel) :
    androidx.recyclerview.widget.ListAdapter<Message, DetailChatAdapter.DetailChatViewHolder>(
        DiffCallback
    ) {

    class DetailChatViewHolder(var binding: ItemDetailChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            message: Message, viewModel: DetailChatViewModel
        ) {
            binding.message = message

            binding.imageChatReceived.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    NavigationDirections.actionGlobalImageDialog(
                        message.image as String
                    )
                ).onClick(binding.imageChatReceived)
            }

            binding.imageChatSend.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    NavigationDirections.actionGlobalImageDialog(
                        message.image as String
                    )
                ).onClick(binding.imageChatReceived)
            }


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
        //右邊的訊息
        if (message.id == UserManager.userId) {
            //訊息是空的不顯示
            if (message.text.isNullOrEmpty()){
                holder.binding.textviewChatReceived.visibility = View.GONE
                holder.binding.textChatReceivedTime.visibility = View.GONE
                holder.binding.textChatSentTime.visibility = View.GONE
                holder.binding.textviewChatSent.visibility = View.GONE
                holder.binding.imageChatSender.visibility = View.GONE
            } else {
                holder.binding.textviewChatReceived.visibility = View.VISIBLE
                holder.binding.textChatReceivedTime.visibility = View.VISIBLE
                holder.binding.textChatSentTime.visibility = View.GONE
                holder.binding.textviewChatSent.visibility = View.GONE
                holder.binding.imageChatSender.visibility = View.GONE
            }
            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.binding.imageChatSend.visibility = View.GONE
                holder.binding.imageChatReceived.visibility = View.GONE
                holder.binding.imageChatReceivedTime.visibility = View.GONE
                holder.binding.imageChatSentTime.visibility = View.GONE
            } else {
                holder.binding.imageChatSend.visibility = View.GONE
                holder.binding.imageChatReceived.visibility = View.VISIBLE
                holder.binding.imageChatReceivedTime.visibility = View.VISIBLE
                holder.binding.imageChatSentTime.visibility = View.GONE
            }

        //左邊的訊息
        } else {
            //訊息是空的不顯示
            if (message.text.isNullOrEmpty()){
                holder.binding.textviewChatReceived.visibility = View.GONE
                holder.binding.textChatReceivedTime.visibility = View.GONE
                holder.binding.textChatSentTime.visibility = View.GONE
                holder.binding.textviewChatSent.visibility = View.GONE
                holder.binding.imageChatSender.visibility = View.GONE
            } else {
                holder.binding.textviewChatReceived.visibility = View.GONE
                holder.binding.textChatReceivedTime.visibility = View.GONE
                holder.binding.textChatSentTime.visibility = View.VISIBLE
                holder.binding.textviewChatSent.visibility = View.VISIBLE
                holder.binding.imageChatSender.visibility = View.VISIBLE
            }

            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.binding.imageChatSend.visibility = View.GONE
                holder.binding.imageChatReceived.visibility = View.GONE
                holder.binding.imageChatReceivedTime.visibility = View.GONE
                holder.binding.imageChatSentTime.visibility = View.GONE
            } else {
                holder.binding.imageChatSend.visibility = View.VISIBLE
                holder.binding.imageChatReceived.visibility = View.GONE
                holder.binding.imageChatReceivedTime.visibility = View.GONE
                holder.binding.imageChatSentTime.visibility = View.VISIBLE
            }

        }
        holder.bind(message, viewModel)
    }
}