package com.eric.phoneauction.homeFragment

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.databinding.ItemHomeGirdBinding
import com.eric.phoneauction.util.Util

class HomeAdapter( val onClickListener: OnClickListener, val viewModel: HomeViewModel ) : androidx.recyclerview.widget.ListAdapter<Event, HomeAdapter.HomeViewHolder>(
    DiffCallback
) {

    class HomeViewHolder(private var binding: ItemHomeGirdBinding):
        RecyclerView.ViewHolder(binding.root) {

        lateinit var timer: CountDownTimer


        fun bind(event: Event, viewModel: HomeViewModel) {
            binding.event = event
            binding.viewModel = viewModel
            binding.executePendingBindings()

             if (event.tag == "直購") {
                 binding.textHomeTime.visibility = View.GONE
            }

            timer = object : CountDownTimer(event.endTime, ONE_SECOND) {
                override fun onFinish() {
                    TODO("Not yet implemented")
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

        fun timerStart(){
            timer.start()
        }

        fun timerStop(){
            timer.cancel()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        const val ONE_SECOND = 1000L
        const val DONE = 0L
        const val ONE_DAY: Long = (1000 * 60 * 60 * 24)
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeGirdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(event)
        }
        holder.bind(event, viewModel)
    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }

    override fun onViewDetachedFromWindow(holder: HomeViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.timerStop()

    }

    override fun onViewAttachedToWindow(holder: HomeViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.timerStart()
    }

}