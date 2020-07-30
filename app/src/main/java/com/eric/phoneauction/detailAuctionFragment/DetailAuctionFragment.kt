package com.eric.phoneauction.detailAuctionFragment

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
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentDetailAuctionBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.homeFragment.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class DetailAuctionFragment : Fragment() {

    lateinit var binding: FragmentDetailAuctionBinding
    private val viewModel by viewModels<DetailAuctionViewModel> {
        getVmFactory(
            DetailAuctionFragmentArgs.fromBundle(requireArguments()).event
        )
    }
    private val homeViewModel by viewModels<HomeViewModel> { getVmFactory() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DetailAuctionFragmentArgs.fromBundle(requireArguments()).event

        binding = FragmentDetailAuctionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, homeViewModel)
        adapter.setHasStableIds(true)
        binding.recyclerDetailAuctionAlsoLike.adapter = adapter
        binding.recyclerDetailAuction.adapter = DetailGalleryAdapter()
        binding.recyclerDetailAuctionCircles.adapter = DetailCircleAdapter()

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailAuction)
        }

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigateUp()
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
            product.images?.size?.times(100).let {
                if (it != null) {
                    binding.recyclerDetailAuction
                        .scrollToPosition(it)
                }
            }

            viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
                (binding.recyclerDetailAuctionCircles.adapter as DetailCircleAdapter).selectedPosition.value =
                    (it % (product.images.size))
            })
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.tag) {
                    "拍賣" -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalDetailAuctionFragment(
                                it
                            )
                        )
                        viewModel.onDetailNavigated()
                    }
                    "直購" -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalDetailDirectFragment(
                                it
                            )
                        )
                        viewModel.onDetailNavigated()
                    }
                }
            }
        })

        viewModel.navigateToDetailChat.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.postChatRoom(viewModel.getChatRoom())
                findNavController().navigate(DetailAuctionFragmentDirections.actionDetailAuctionFragmentToDetailChatFragment(it))
                viewModel.onDetailChatNavigated()
            }
        })

        viewModel.navigateToAuction.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalAuctionDialog(it))
                viewModel.onAuctionNavigated()
            }
        })

        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        viewModel.countDown.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.textDetailAuctionTime.text = it
            }
        })

        binding.buttonRePost.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalPostFragment())
        }

        binding.imageViewDetailAuctionCollection.setOnClickListener {
            viewModel.postCollection(viewModel.addCollection(true), UserManager.user)
            binding.imageViewDetailAuctionCollection.visibility = View.GONE
            binding.imageViewDetailAuctionCollectioned.visibility = View.VISIBLE
            findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.COLLECTION_SUCCESS))
        }

        binding.imageViewDetailAuctionCollectioned.setOnClickListener {
            viewModel.postCollection(viewModel.addCollection(false), UserManager.user)
            binding.imageViewDetailAuctionCollection.visibility = View.VISIBLE
            binding.imageViewDetailAuctionCollectioned.visibility = View.GONE
            findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.UN_COLLECTION_SUCCESS))
        }

        //最近商品成交價
        viewModel.averageEvents.observe(viewLifecycleOwner, Observer { list ->
            list?.let { event ->
                viewModel.averagePrice.value = event.map { it.price }.average().toInt()
            }
        })

        binding.imageAuctionQuestion.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.AVERAGE_PRICE))
        }

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
