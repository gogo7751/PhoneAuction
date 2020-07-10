package com.eric.phoneauction.auctionDialog

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.AuctionDialogBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class AuctionDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<AuctionViewModel> { getVmFactory(AuctionDialogArgs.fromBundle(requireArguments()).event) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AuctionDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        binding.textAuctionNt.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.editAuctionPrice.paintFlags = Paint.UNDERLINE_TEXT_FLAG



        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })


        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            viewModel.postAuction(it, viewModel.price.value as Int)
            findNavController().navigate(AuctionDialogDirections.actionAuctionDialogToCheckSuccessAuctionFragment())
            viewModel.leave()
        })




        return binding.root
    }

}
