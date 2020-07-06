package com.eric.phoneauction.homeFragment

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.TimeUtil
import com.eric.phoneauction.databinding.ItemHomeGirdBinding

class HomeAdapter(val viewModel: HomeViewModel) : androidx.recyclerview.widget.ListAdapter<Event, HomeAdapter.HomeViewHolder>(
    DiffCallback
) {

    class HomeViewHolder(private var binding: ItemHomeGirdBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, viewModel: HomeViewModel) {
            binding.event = event
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


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeGirdBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, viewModel)
    }

}