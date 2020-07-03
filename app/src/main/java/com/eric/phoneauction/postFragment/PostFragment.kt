package com.eric.phoneauction.postFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R


import com.eric.phoneauction.databinding.PostFragmentBinding
import kotlinx.android.synthetic.main.activity_main.*

class PostFragment : Fragment() {

    lateinit var binding: PostFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostFragmentBinding.inflate(inflater, container, false)




        binding.spinnerBrand.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.brand_list)
        )

        binding.imageViewPostBack.setOnClickListener {
            findNavController().navigateUp()
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
    }

}
