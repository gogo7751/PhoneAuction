package com.eric.phoneauction.homeFragment

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eric.phoneauction.data.TimeUtil
import com.eric.phoneauction.databinding.HomeFragmentBinding
import com.eric.phoneauction.databinding.ItemHomeGirdBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.toDisplayFormat
import com.eric.phoneauction.util.Logger
import okhttp3.internal.notifyAll


class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = HomeFragmentBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(viewModel)
        binding.recyclerviewHome.adapter = adapter

        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("events", "$it")
            }
        })

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshHome.isRefreshing = it
            }
        })

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })

        binding.buttonAuction.setOnClickListener {

        }

        binding.buttonDirect.setOnClickListener {

        }

        return binding.root
    }


}
