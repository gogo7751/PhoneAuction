package com.eric.phoneauction.notification

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemNotificationBinding
import com.eric.phoneauction.util.Util.getString


class NotificationAdapter(val viewModel: NotificationViewModel) :
    androidx.recyclerview.widget.ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(
        DiffCallback
    ) {

    class NotificationViewHolder(private var binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notification: Notification,
            viewModel: NotificationViewModel
        ) {
            binding.notification = notification
            binding.textNotificationDelete.setOnClickListener {
                UserManager.userId?.let { userId ->
                    viewModel.deleteNotification(
                        notification.id,
                        userId
                    )
                }
            }

            binding.imageNotification.setOnClickListener {
                if (notification.event?.tag == getString(R.string.auction_tag)) {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailAuctionFragment(
                            notification.event as Event
                        )
                    ).onClick(binding.imageNotification)
                } else {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailDirectFragment(
                            notification.event as Event
                        )
                    ).onClick(binding.imageNotification)
                }
            }

            var flag = true
            binding.textNotificationTitle.setOnClickListener {
                if (flag == true) {
                    flag = false
                    binding.textNotificationTitle.isSingleLine = false
                    binding.textNotificationTitle.ellipsize = null
                } else {
                    flag = true
                    binding.textNotificationTitle.isSingleLine = true
                    binding.textNotificationTitle.ellipsize = TextUtils.TruncateAt.END
                }

            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification, viewModel)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}