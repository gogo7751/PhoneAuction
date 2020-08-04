package com.eric.phoneauction.home

import android.icu.util.Calendar
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.databinding.ItemHomeGirdBinding
import com.eric.phoneauction.util.Util

class HomeAdapter( val onClickListener: OnClickListener, val viewModel: HomeViewModel ) : androidx.recyclerview.widget.ListAdapter<Event, HomeAdapter.HomeViewHolder>(
    DiffCallback
) {

    class HomeViewHolder(private var binding: ItemHomeGirdBinding):
        RecyclerView.ViewHolder(binding.root) {

        lateinit var timer: CountDownTimer


        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(event: Event, viewModel: HomeViewModel) {
            binding.event = event
            binding.viewModel = viewModel
            binding.executePendingBindings()

             if (event.tag == "直購") {
                 binding.textHomeTime.visibility = View.GONE
            }

            fun getNotification(title: String): Notification {
                return Notification(
                    id = "",
                    title = title,
                    time = -1,
                    brand = event.brand,
                    name = event.productName,
                    image = event.images.component1(),
                    storage = event.storage,
                    visibility = true,
                    event = event.apply { event.deal = false }
                )
            }


            val millsTime = event.endTime.minus(Calendar.getInstance().timeInMillis)


            timer = object : CountDownTimer(millsTime, ONE_SECOND) {
                override fun onFinish() {
                    viewModel.finishAuction(event)
                    if (event.buyUser != "") {
                        viewModel.postNotification(getNotification("恭喜您得標!"), event.buyUser)
                        viewModel.postNotification(getNotification("拍賣完成,請與買家聯絡完成出貨!"), event.userId)
                    } else if (event.tag != "拍賣"){
                        viewModel.postNotification(getNotification("商品已過期"), event.userId)
                    } else {
                        viewModel.postNotification(getNotification("流標"), event.userId)
                    }
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

    @RequiresApi(Build.VERSION_CODES.N)
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}