package com.eric.phoneauction.onAuction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemOnAuctionBinding
import com.eric.phoneauction.util.Util.getString

class OnAuctionAdapter :
    androidx.recyclerview.widget.ListAdapter<Event, OnAuctionAdapter.OnAuctionViewHolder>(
        DiffCallback
    ) {

    class OnAuctionViewHolder(private var binding: ItemOnAuctionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: Event
        ) {
            binding.event = event

            binding.imageOnAuction.setOnClickListener {
                if (event.tag == getString(R.string.auction_tag)) {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailAuctionFragment(
                            event
                        )
                    ).onClick(binding.imageOnAuction)
                } else {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailDirectFragment(
                            event
                        )
                    ).onClick(binding.imageOnAuction)
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
    ): OnAuctionViewHolder {
        return OnAuctionViewHolder(
            ItemOnAuctionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnAuctionViewHolder, position: Int) {
        val event = getItem(position)
        when (event.buyerId == UserManager.userId) {
            true -> holder.itemView.visibility = View.VISIBLE
            false -> {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams.height = 0
            }
        }
        holder.bind(event)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}