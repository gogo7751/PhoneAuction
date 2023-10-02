package com.eric.phoneauction.detailDirectFragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.R
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentDetailDirectBinding
import com.eric.phoneauction.detailAuctionFragment.DetailCircleAdapter
import com.eric.phoneauction.detailAuctionFragment.DetailGalleryAdapter
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailDirectFragment : Fragment() {
    lateinit var binding: FragmentDetailDirectBinding
    private val viewModel by viewModels<DetailDirectViewModel> { getVmFactory(DetailDirectFragmentArgs.fromBundle(requireArguments()).event) }
    private val homeViewModel by viewModels<HomeViewModel> { getVmFactory() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DetailDirectFragmentArgs.fromBundle(requireArguments()).event

        binding = FragmentDetailDirectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, homeViewModel)
        adapter.setHasStableIds(true)
        binding.recyclerDetailDirectAlsoLike.adapter = adapter
        binding.recyclerDetailDirect.adapter = DetailGalleryAdapter()
        binding.recyclerDetailDirectCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailDirect)
        }

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigateUp()
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
            event.images.size.times(100).let {
                binding.recyclerDetailDirect
                    .scrollToPosition(it)
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

        binding.imageViewDetailDirectCollection.setOnClickListener {
            viewModel.postCollection(viewModel.addCollection(true), UserManager.user)
            binding.imageViewDetailDirectCollection.visibility = View.GONE
            binding.imageViewDetailDirectCollectioned.visibility = View.VISIBLE
            findNavController().navigate(NavigationDirections.navigateToMessageDialog(
                MessageDialog.MessageType.COLLECTION_SUCCESS))
        }

        binding.imageViewDetailDirectCollectioned.setOnClickListener {
            viewModel.postCollection(viewModel.addCollection(false), UserManager.user)
            binding.imageViewDetailDirectCollection.visibility = View.VISIBLE
            binding.imageViewDetailDirectCollectioned.visibility = View.GONE
            findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.UN_COLLECTION_SUCCESS))
        }

        //最近商品成交價
        viewModel.averageEvents.observe(viewLifecycleOwner, Observer { list ->
            list?.let { event ->
                viewModel.averagePrice.value = event.map { it.price }.average().toInt()
            }
        })

        binding.imageDirectQuestion.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.AVERAGE_PRICE))
        }

        viewModel.timerStart()
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavView).visibility = View.GONE
        return binding.root
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        viewModel.timerStop()
    }

}
