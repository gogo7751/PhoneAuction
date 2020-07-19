package com.eric.phoneauction.purchasedFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemPurchasedBinding
import com.eric.phoneauction.util.Logger
import kotlinx.android.synthetic.main.item_purchased.view.*

class PurchasedAdapter(val viewModel: PurchasedViewModel) :
    androidx.recyclerview.widget.ListAdapter<Event, PurchasedAdapter.PurchasedViewHolder>(
        DiffCallback
    ) {

    class PurchasedViewHolder(private var binding: ItemPurchasedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: Event,
            viewModel: PurchasedViewModel
        ) {
            binding.event = event

//            binding.textNotificationDelete.setOnClickListener {
//                UserManager.userId?.let { userId ->
//                    viewModel.deleteNotification(
//                        notification.id,
//                        userId
//                    )
//                }
//            }

//            when (event.buyUser == UserManager.user.id && !event.deal) {
//                true -> binding.layoutProfileAll.visibility = View.VISIBLE
//                false -> binding.layoutProfileAll.visibility = View.GONE
//            }


            binding.imagePurchased.setOnClickListener {
                if (event.tag == "拍賣") {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailAuctionFragment(
                            event
                        )
                    ).onClick(binding.imagePurchased)
                } else {
                    Navigation.createNavigateOnClickListener(
                        NavigationDirections.actionGlobalDetailDirectFragment(
                            event
                        )
                    ).onClick(binding.imagePurchased)
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
    ): PurchasedViewHolder {
        return PurchasedViewHolder(
            ItemPurchasedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PurchasedViewHolder, position: Int) {
        val event = getItem(position)
        when (event.buyUser == UserManager.userId) {
            true -> holder.itemView.visibility = View.VISIBLE
            false -> {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams.height = 0
            }
        }
        holder.bind(event, viewModel)
    }

}