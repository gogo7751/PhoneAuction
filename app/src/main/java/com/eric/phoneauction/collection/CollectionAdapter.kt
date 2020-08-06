package com.eric.phoneauction.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Collection
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ItemCollectionBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.util.Util.getString

class CollectionAdapter(val viewModel: CollectionViewModel) :
    androidx.recyclerview.widget.ListAdapter<Collection, CollectionAdapter.CollectionViewHolder>(
        DiffCallback
    ) {

    class CollectionViewHolder(private var binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            collection: Collection,
            viewModel: CollectionViewModel
        ) {
            binding.collection = collection

            binding.imageCollection.setOnClickListener {
                when (collection.event?.tag) {
                    getString(R.string.auction_tag) -> {
                        Navigation.createNavigateOnClickListener(
                            NavigationDirections.actionGlobalDetailAuctionFragment(
                                collection.event as Event
                            )
                        ).onClick(binding.imageCollection)
                    }
                    else -> {
                        Navigation.createNavigateOnClickListener(
                            NavigationDirections.actionGlobalDetailDirectFragment(
                                collection.event as Event
                            )
                        ).onClick(binding.imageCollection)
                    }
                }
            }

            binding.textCollectionDelete.setOnClickListener {
                viewModel.postCollection(collection.apply { collection.visibility = false }, UserManager.user)
                Navigation.createNavigateOnClickListener(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.UN_COLLECTION_SUCCESS))
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionViewHolder {
        return CollectionViewHolder(
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = getItem(position)
        holder.bind(collection, viewModel)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}