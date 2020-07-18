package com.eric.phoneauction.detailDirectFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DetailDirectFragmentBinding
import com.eric.phoneauction.detailAuctionFragment.DetailAuctionFragmentArgs
import com.eric.phoneauction.detailAuctionFragment.DetailAuctionViewModel
import com.eric.phoneauction.detailAuctionFragment.DetailCircleAdapter
import com.eric.phoneauction.detailAuctionFragment.DetailGalleryAdapter
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.eric.phoneauction.util.Logger
import kotlinx.android.synthetic.main.activity_main.*


class DetailDirectFragment : Fragment() {
    lateinit var binding: DetailDirectFragmentBinding
    private val viewModel by viewModels<DetailDirectViewModel> { getVmFactory(DetailDirectFragmentArgs.fromBundle(requireArguments()).event) }
    private val homeViewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DetailDirectFragmentArgs.fromBundle(requireArguments()).event

        binding = DetailDirectFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, homeViewModel)
        binding.recyclerDetailDirectAlsoLike.adapter = adapter
        binding.recyclerDetailDirect.adapter = DetailGalleryAdapter()
        binding.recyclerDetailDirectCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailDirect)
        }

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }
        })

        binding.recyclerDetailDirect.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailDirect.layoutManager,
                linearSnapHelper
            )
        }

        // set the initial position to the center of infinite gallery
        viewModel.event.value?.let { event ->
            event.images?.size?.times(100).let {
                if (it != null) {
                    binding.recyclerDetailDirect
                        .scrollToPosition(it)
                }
            }
            viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
                (binding.recyclerDetailDirectCircles.adapter as DetailCircleAdapter).selectedPosition.value = (it % (event.images?.size
                    ?: 0))
            })
        }


        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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

        viewModel.navigateToDirect.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDirectDialog(it))
                viewModel.onDirectNavigated()
            }
        })

        viewModel.navigateToDetailChat.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.postChatRoom(viewModel.getChatRoom())
                findNavController().navigate(DetailDirectFragmentDirections.actionDetailDirectFragmentToDetailChatFragment(it))
                viewModel.onDetailChatNavigated()
            }
        })

        viewModel.countDown.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.textDetailDirectTime.text = it
            }
        })

        viewModel.timerStart()
        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return binding.root
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        viewModel.timerStop()
    }

}
