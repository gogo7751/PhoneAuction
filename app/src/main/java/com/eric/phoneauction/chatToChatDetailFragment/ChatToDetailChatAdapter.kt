package com.eric.phoneauction.chatToChatDetailFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemChatToDetailChatBinding
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_chat_to_detail_chat.view.*

class ChatToDetailChatAdapter(val viewModel: ChatToDetailChatViewModel) :
    androidx.recyclerview.widget.ListAdapter<Message, ChatToDetailChatAdapter.DetailChatViewHolder>(
        DiffCallback
    ) {

    class DetailChatViewHolder(private var binding: ItemChatToDetailChatBinding) :
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
                holder.itemView.textview_chatToDetail_received.visibility = View.GONE
                holder.itemView.text_chatToDetail_receivedTime.visibility =View.GONE
                holder.itemView.text_chatToDetail_sentTime.visibility = View.GONE
                holder.itemView.textview_chatToDetail_sent.visibility = View.GONE
                holder.itemView.image_chatToDetail_sender.visibility = View.GONE
            } else {
                holder.itemView.textview_chatToDetail_received.visibility = View.VISIBLE
                holder.itemView.text_chatToDetail_receivedTime.visibility =View.VISIBLE
                holder.itemView.text_chatToDetail_sentTime.visibility = View.GONE
                holder.itemView.textview_chatToDetail_sent.visibility = View.GONE
                holder.itemView.image_chatToDetail_sender.visibility = View.GONE
            }
            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.itemView.cardView_chatToDetail_send.visibility = View.GONE
                holder.itemView.cardView_chatToDetail_received.visibility = View.GONE
                holder.itemView.image_chatToDetail_receivedTime.visibility = View.GONE
                holder.itemView.image_chatToDetail_sentTime.visibility = View.GONE
            } else {
                holder.itemView.cardView_chatToDetail_send.visibility = View.GONE
                holder.itemView.cardView_chatToDetail_received.visibility = View.VISIBLE
                holder.itemView.image_chatToDetail_receivedTime.visibility = View.VISIBLE
                holder.itemView.image_chatToDetail_sentTime.visibility = View.GONE
            }
        //左邊的訊息
        } else {
            //訊息是空的不顯示
            if (message.text.isNullOrEmpty()){
                holder.itemView.textview_chatToDetail_received.visibility = View.GONE
                holder.itemView.text_chatToDetail_receivedTime.visibility =View.GONE
                holder.itemView.text_chatToDetail_sentTime.visibility = View.GONE
                holder.itemView.textview_chatToDetail_sent.visibility = View.GONE
            } else {
                holder.itemView.textview_chatToDetail_received.visibility = View.GONE
                holder.itemView.text_chatToDetail_sentTime.visibility = View.VISIBLE
                holder.itemView.text_chatToDetail_receivedTime.visibility = View.GONE
                holder.itemView.cardView_chatToDetail_received.visibility = View.GONE
                holder.itemView.textview_chatToDetail_sent.visibility = View.VISIBLE
            }
            //圖片是空的不顯示
            if (message.image.isNullOrEmpty()){
                holder.itemView.cardView_chatToDetail_send.visibility = View.GONE
                holder.itemView.cardView_chatToDetail_received.visibility = View.GONE
                holder.itemView.image_chatToDetail_receivedTime.visibility = View.GONE
                holder.itemView.image_chatToDetail_sentTime.visibility = View.GONE
            } else {
                holder.itemView.cardView_chatToDetail_send.visibility = View.VISIBLE
                holder.itemView.cardView_chatToDetail_received.visibility = View.GONE
                holder.itemView.image_chatToDetail_receivedTime.visibility = View.GONE
                holder.itemView.image_chatToDetail_sentTime.visibility = View.VISIBLE
            }
        }
        holder.bind(message, viewModel)
    }


}