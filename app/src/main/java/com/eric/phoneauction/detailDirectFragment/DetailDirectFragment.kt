package com.eric.phoneauction.detailDirectFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DetailDirectFragmentBinding


class DetailDirectFragment : Fragment() {
    lateinit var binding: DetailDirectFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailDirectFragmentBinding.inflate(inflater, container, false)



        return binding.root
    }


}
