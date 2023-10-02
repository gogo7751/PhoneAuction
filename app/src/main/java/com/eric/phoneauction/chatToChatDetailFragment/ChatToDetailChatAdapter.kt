package com.eric.phoneauction.chatToChatDetailFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemChatToDetailChatBinding

class ChatToDetailChatAdapter(val viewModel: ChatToDetailChatViewModel) :
    androidx.recyclerview.widget.ListAdapter<Message, ChatToDetailChatAdapter.DetailChatViewHolder>(
        DiffCallback
    ) {

    class DetailChatViewHolder(var binding: ItemChatToDetailChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            message: Message, viewModel: ChatToDetailChatViewModel
        ) {
            binding.message = message

            binding.cardViewChatToDetailReceived.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    NavigationDirections.actionGlobalImageDialog(
                        message.image as String
                    )
                ).onClick(binding.cardViewChatToDetailReceived)
            }

            binding.cardViewChatToDetailSend.setOnClickListener {
                Navigation.createNavigateOnClickListener(
                    NavigationDirections.actionGlobalImageDialog(
                        message.image as String
                    )
                ).onClick(binding.cardViewChatToDetailReceived)
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
            ItemChatToDetailChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailChatViewHolder, position: Int) {
        val message = getItem(position)
        //右邊的訊息
        if (message.id == UserManager.userId) {
            //訊息是空的不顯示
            if (message.text.isNullOrEmpty()){
                holder.binding.textviewChatToDetailReceived.visibility = View.GONE
                holder.binding.textChatToDetailReceivedTime.visibility =View.GONE
                holder.binding.textChatToDetailSentTime.visibility = View.GONE
                holder.binding.textviewChatToDetailSent.visibility = View.GONE
                holder.binding.imageChatToDetailSender.visibility = View.GONE
            } else {
                holder.binding.textviewChatToDetailReceived.visibility = View.VISIBLE
                holder.binding.textChatToDetailReceivedTime.visibility =View.VISIBLE
                holder.binding.textChatToDetailSentTime.visibility = View.GONE
                holder.binding.textviewChatToDetailSent.visibility = View.GONE
                holder.binding.imageChatToDetailSender.visibility = View.GONE
            }
            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.binding.cardViewChatToDetailSend.visibility = View.GONE
                holder.binding.cardViewChatToDetailReceived.visibility = View.GONE
                holder.binding.imageChatToDetailReceivedTime.visibility = View.GONE
                holder.binding.imageChatToDetailSentTime.visibility = View.GONE
            } else {
                holder.binding.cardViewChatToDetailSend.visibility = View.GONE
                holder.binding.cardViewChatToDetailReceived.visibility = View.VISIBLE
                holder.binding.imageChatToDetailReceivedTime.visibility = View.VISIBLE
                holder.binding.imageChatToDetailSentTime.visibility = View.GONE
            }
        //左邊的訊息
        } else {
            //訊息是空的不顯示
            if (message.text.isNullOrEmpty()){
                holder.binding.textviewChatToDetailReceived.visibility = View.GONE
                holder.binding.textChatToDetailReceivedTime.visibility =View.GONE
                holder.binding.textChatToDetailSentTime.visibility = View.GONE
                holder.binding.textviewChatToDetailSent.visibility = View.GONE
            } else {
                holder.binding.textviewChatToDetailReceived.visibility = View.GONE
                holder.binding.textChatToDetailSentTime.visibility = View.VISIBLE
                holder.binding.textChatToDetailReceivedTime.visibility = View.GONE
                holder.binding.cardViewChatToDetailReceived.visibility = View.GONE
                holder.binding.textviewChatToDetailSent.visibility = View.VISIBLE
            }
            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.binding.cardViewChatToDetailSend.visibility = View.GONE
                holder.binding.cardViewChatToDetailReceived.visibility = View.GONE
                holder.binding.imageChatToDetailReceivedTime.visibility = View.GONE
                holder.binding.imageChatToDetailSentTime.visibility = View.GONE
            } else {
                holder.binding.cardViewChatToDetailSend.visibility = View.VISIBLE
                holder.binding.cardViewChatToDetailReceived.visibility = View.GONE
                holder.binding.imageChatToDetailReceivedTime.visibility = View.GONE
                holder.binding.imageChatToDetailSentTime.visibility = View.VISIBLE
            }
        }
        holder.bind(message, viewModel)
    }


}