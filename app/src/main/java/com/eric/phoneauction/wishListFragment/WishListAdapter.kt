package com.eric.phoneauction.wishListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.data.WishList
import com.eric.phoneauction.databinding.ItemWishListBinding

class WishListAdapter(val viewModel: WishListViewModel) :
    androidx.recyclerview.widget.ListAdapter<WishList, WishListAdapter.WishListViewHolder>(
        DiffCallback
    ) {

    class WishListViewHolder(private var binding: ItemWishListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            wishList: WishList, viewModel: WishListViewModel
        ) {
            binding.wishList = wishList
            binding.textWishListDelete.setOnClickListener {
                viewModel.updateWishList(wishList.id)
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<WishList>() {
        override fun areItemsTheSame(oldItem: WishList, newItem: WishList): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WishList, newItem: WishList): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishListViewHolder {
        return WishListViewHolder(
            ItemWishListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val wishList = getItem(position)
        holder.bind(wishList, viewModel)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}