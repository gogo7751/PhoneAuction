package com.eric.phoneauction.directDialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.R
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.DialogDirectFragmentBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 */
class DirectDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<DirectViewModel> { getVmFactory(DirectDialogArgs.fromBundle(requireArguments()).event) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogDirectFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onLeaveCompleted()
            }
        })


        viewModel.navigateToCheckoutSuccess.observe(viewLifecycleOwner, Observer {
            viewModel.postAuction(it)
            viewModel.postNotification(viewModel.getNotification("您的商品已被購買,請盡快進行出貨事宜!"), viewModel.event.value?.userId.toString())
            viewModel.postNotification(viewModel.getNotification("恭喜您購買成功,請與賣家聯絡並完成付款!"), UserManager.userId as String)
            findNavController().navigate(DirectDialogDirections.actionDirectDialogToCheckoutSuccessDirectFragment())
            viewModel.leave()
        })

        binding.imageDirectClose.setOnClickListener {
            dismiss()
        }




        //畫面展開
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }



        return binding.root
    }

}
