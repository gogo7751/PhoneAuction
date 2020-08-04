package com.eric.phoneauction.detailAuction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.MainActivity
import com.eric.phoneauction.databinding.ItemDetailCircleBinding

class DetailCircleAdapter : RecyclerView.Adapter<DetailCircleAdapter.ImageViewHolder>() {

    private lateinit var context: Context
    private var count = 0
    var selectedPosition = MutableLiveData<Int>()

    class ImageViewHolder(val binding: ItemDetailCircleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, selectedPosition: MutableLiveData<Int>) {

            selectedPosition.observe(context as MainActivity, Observer {
                binding.isSelected = it == adapterPosition
                binding.executePendingBindings()
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context = parent.context
        return ImageViewHolder(ItemDetailCircleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(context, selectedPosition)
    }

    override fun getItemCount(): Int {
        return count
    }

    /**
     * Submit data list and refresh adapter by [notifyDataSetChanged]
     * @param count: set up count of circles
     */
    fun submitCount(count: Int) {
        this.count = count
        notifyDataSetChanged()
    }

}