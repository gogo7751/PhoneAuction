package com.eric.phoneauction.detailDirectFragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DetailDirectFragmentBinding
import com.eric.phoneauction.detailAuctionFragment.DetailCircleAdapter
import com.eric.phoneauction.detailAuctionFragment.DetailGalleryAdapter
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeAdapter
import kotlinx.android.synthetic.main.activity_main.*


class DetailDirectFragment : Fragment() {
    lateinit var binding: DetailDirectFragmentBinding
    private val viewModel by viewModels<DetailDirectViewModel> { getVmFactory(DetailDirectFragmentArgs.fromBundle(requireArguments()).event) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailDirectFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        })
        binding.recyclerDetailDirectAlsoLike.adapter = adapter
        binding.recyclerDetailDirect.adapter = DetailGalleryAdapter()
        binding.recyclerDetailDirectCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailDirect)
        }

        binding.recyclerDetailDirect.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailDirect.layoutManager,
                linearSnapHelper
            )
        }

        // set the initial position to the center of infinite gallery
        viewModel.event.value?.let { product ->
            product.images?.size?.times(100)?.let {
                binding.recyclerDetailDirect
                    .scrollToPosition(it)
            }

            viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
                (binding.recyclerDetailDirectCircles.adapter as DetailCircleAdapter).selectedPosition.value = (it % product.images?.size as Int)
            })
        }

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }
        })

        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.tag) {
                    "拍賣" -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailAuctionFragment(it, it.tag))
                        viewModel.onDetailNavigated()
                    }
                    "直購" -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailDirectFragment(it, it.tag))
                        viewModel.onDetailNavigated()
                    }
                }
            }
        })

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return binding.root
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
    }

}
