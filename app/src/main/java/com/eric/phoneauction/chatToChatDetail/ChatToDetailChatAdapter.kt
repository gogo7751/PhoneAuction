package com.eric.phoneauction.chatToChatDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemChatToDetailChatBinding
import kotlinx.android.synthetic.main.item_chat_to_detail_chat.view.*

class ChatToDetailChatAdapter(val viewModel: ChatToDetailChatViewModel) : ListAdapter<Message, ChatToDetailChatAdapter.DetailChatViewHolder>(
        DiffCallback) {

    class DetailChatViewHolder(private var binding: ItemChatToDetailChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            message: Message,
            viewModel: ChatToDetailChatViewModel
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

            binding.isOwner = viewModel.isUserManager(message.id)
            binding.isTextEmpty = message.text.isNullOrEmpty()
            binding.isImageEmpty = message.image.isNullOrEmpty()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailChatViewHolder {
        return DetailChatViewHolder(
            ItemChatToDetailChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailChatViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message, viewModel)
    }
}