package com.eric.phoneauction.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.ItemPostSpinnerBinding

class PostSpinnerAdapter(private val strings: Array<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemPostSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        if (position == 0){
            binding.title = PhoneAuctionApplication.instance.resources.getString(R.string.select_post_spinner)
            binding.textSpinnerTitle.setTextColor(PhoneAuctionApplication.instance.getColor(R.color.gary757575))
        }else {
            binding.title = strings[position-1]
        }
        return binding.root
    }

    override fun getItem(position: Int): Any {
        if (position == 0)
            return 0
        else
            return strings[position-1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return strings.size + 1
    }
}