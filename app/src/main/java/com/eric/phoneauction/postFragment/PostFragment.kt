package com.eric.phoneauction.postFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.PostFragmentBinding

class PostFragment : Fragment() {

    lateinit var binding: PostFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostFragmentBinding.inflate(inflater, container, false)




        return binding.root
    }


}
