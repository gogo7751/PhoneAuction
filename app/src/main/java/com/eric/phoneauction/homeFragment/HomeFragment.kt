package com.eric.phoneauction.homeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.databinding.HomeFragmentBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.toDisplayFormat
import com.eric.phoneauction.util.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat


class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = HomeFragmentBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, viewModel)
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

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.tag) {
                    "拍賣" -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailAuctionFragment(it))
                        viewModel.onDetailNavigated()
                    }
                    "直購" -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailDirectFragment(it))
                        viewModel.onDetailNavigated()
                    }
                }
            }
        })


        binding.buttonAuction.setOnClickListener {
            viewModel.getAuctionResult()
            adapter.notifyDataSetChanged()
        }

        binding.buttonDirect.setOnClickListener {
            viewModel.getDirectResult()
            adapter.notifyDataSetChanged()
        }


        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }


}
