package com.eric.phoneauction.onPostFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemOnAuctionBinding
import com.eric.phoneauction.databinding.ItemOnPostBinding

class OnPostAdapter :
    androidx.recyclerview.widget.ListAdapter<Event, OnPostAdapter.OnPostViewHolder>(
        DiffCallback
    ) {

    class OnPostViewHolder(private var binding: ItemOnPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: Event
        ) {
            binding.event = event

            binding.imageOnPost.setOnClickListener {
                if (event.tag == "拍賣") {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailAuctionFragment(
                            event
                        )
                    ).onClick(binding.imageOnPost)
                } else {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailDirectFragment(
                            event
                        )
                    ).onClick(binding.imageOnPost)
                }
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnPostViewHolder {
        return OnPostViewHolder(
            ItemOnPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnPostViewHolder, position: Int) {
        val event = getItem(position)
        when (event.userId == UserManager.userId) {
            true -> holder.itemView.visibility = View.VISIBLE
            false -> {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams.height = 0
            }
        }
        holder.bind(event)
    }

}