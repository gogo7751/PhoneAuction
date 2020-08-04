package com.eric.phoneauction.wishList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.databinding.FragmentWishListBinding
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class WishListFragment : Fragment() {

    val viewModel: WishListViewModel by viewModels<WishListViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWishListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = WishListAdapter(viewModel)
        adapter.setHasStableIds(true)
        adapter.notifyDataSetChanged()
        binding.recyclerviewWishList.adapter = adapter

        viewModel.wishLists.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.textNoContent.visibility = View.VISIBLE
            } else {
                binding.textNoContent.visibility = View.GONE
            }
        })

        binding.imageWishListBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageWishListQuestion.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.WISH))
        }




        return binding.root
    }

}
