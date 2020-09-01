package com.eric.phoneauction.detailChat

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
        holder.bind(message, viewModel)
    }
}