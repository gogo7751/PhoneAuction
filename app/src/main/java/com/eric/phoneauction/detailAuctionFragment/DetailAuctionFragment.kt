package com.eric.phoneauction.detailAuctionFragment

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
import com.eric.phoneauction.databinding.DetailAuctionFragmentBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.homeFragment.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class DetailAuctionFragment : Fragment() {

    lateinit var binding: DetailAuctionFragmentBinding
    private val viewModel by viewModels<DetailAuctionViewModel> { getVmFactory(DetailAuctionFragmentArgs.fromBundle(requireArguments()).event) }
    private val homeViewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DetailAuctionFragmentArgs.fromBundle(requireArguments()).event

        binding = DetailAuctionFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, homeViewModel)
        binding.recyclerDetailAuctionAlsoLike.adapter = adapter
        binding.recyclerDetailAuction.adapter = DetailGalleryAdapter()
        binding.recyclerDetailAuctionCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailAuction)
        }

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }
        })

        binding.recyclerDetailAuction.setOnScrollChangeListener { _, _, _, _, _ ->
            viewModel.onGalleryScrollChange(
                binding.recyclerDetailAuction.layoutManager,
                linearSnapHelper
            )
        }

        // set the initial position to the center of infinite gallery
        viewModel.event.value?.let { product ->
            product.images.size.times(100).let {
                binding.recyclerDetailAuction
                    .scrollToPosition(it)
            }

            viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
                (binding.recyclerDetailAuctionCircles.adapter as DetailCircleAdapter).selectedPosition.value = (it % product.images.size)
            })
        }

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


        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToAuction.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalAuctionDialog(it))
                viewModel.onAuctionNavigated()
            }
        })

        viewModel.countDown.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.textDetailAuctionTime.text = it
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
