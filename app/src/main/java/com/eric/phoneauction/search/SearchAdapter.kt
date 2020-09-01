package com.eric.phoneauction.search

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.databinding.ItemSearchBinding
import com.eric.phoneauction.databinding.ItemSearchFooterBinding
import com.eric.phoneauction.util.Util
import com.eric.phoneauction.util.Util.getString

class SearchAdapter( val onClickListener: OnClickListener, val viewModel: SearchViewModel) : androidx.recyclerview.widget.ListAdapter<Event, RecyclerView.ViewHolder>(
    DiffCallback
) {

    class SearchViewHolder(private var binding: ItemSearchBinding):
        RecyclerView.ViewHolder(binding.root) {

        lateinit var timer: CountDownTimer

        fun bind(event: Event, viewModel: SearchViewModel) {
            binding.event = event
            binding.viewModel = viewModel
            binding.executePendingBindings()

            if (event.tag == getString(R.string.direct_tag)) {
                binding.textSearchTime.visibility = View.GONE
            }

            val millsTime = event.endTime.minus(event.createdTime)

            timer = object : CountDownTimer(millsTime, ONE_SECOND) {
                override fun onFinish() {
                }
                override fun onTick(millisUntilFinished: Long) {
                    val sec = millisUntilFinished / ONE_SECOND % 60
                    val min = millisUntilFinished / ONE_SECOND / 60 % 60
                    val hr = millisUntilFinished / ONE_SECOND / 60 / 60 % 24
                    binding.countDown = StringBuilder()
                        .append(Util.lessThenTenPadStart(hr)).append(Util.getString(R.string.hours))
                        .append(Util.lessThenTenPadStart(min)).append(Util.getString(R.string.minutes))
                        .append(Util.lessThenTenPadStart(sec)).append(Util.getString(R.string.seconds)).toString()
                }
            }
        }

        fun timerStart() {
            timer.start()
        }

        fun timerStop() {
            timer.cancel()
        }
        companion object {
            fun from(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)

                return SearchViewHolder(binding)
            }
        }
    }

    class WishViewHolder(private val binding: ItemSearchFooterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): WishViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchFooterBinding.inflate(layoutInflater, parent, false)

                return WishViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        const val ONE_SECOND = 1000L
        const val DONE = 0L
        private const val ITEM_VIEW_TYPE_SEARCH = 0x00
        private const val ITEM_VIEW_TYPE_WISH = 0x01
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            ITEM_VIEW_TYPE_WISH
        } else {
            ITEM_VIEW_TYPE_SEARCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_SEARCH -> SearchViewHolder.from(parent)
            ITEM_VIEW_TYPE_WISH -> WishViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is SearchViewHolder -> {
                val event = getItem(position)
                holder.itemView.setOnClickListener { onClickListener.onClick(event) }
                holder.bind(event, viewModel)
            }

            is WishViewHolder -> {
                holder.bind()
            }
        }
    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is SearchViewHolder -> holder.timerStart()
        }

    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is SearchViewHolder -> holder.timerStop()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}