package com.eric.phoneauction.detailAuction

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
import com.eric.phoneauction.databinding.FragmentDetailAuctionBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.showToast
import com.eric.phoneauction.home.HomeAdapter
import com.eric.phoneauction.home.HomeViewModel
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
            product.images.size.times(100).let {
                binding.recyclerDetailAuction
                    .scrollToPosition(it)
            }

            viewModel.snapPosition.observe(viewLifecycleOwner, Observer {
                it?.let {
                    (binding.recyclerDetailAuctionCircles.adapter as DetailCircleAdapter).selectedPosition.value =
                        (it % (product.images.size))
                }
            })
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.tag) {
                    getString(R.string.auction_tag) -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalDetailAuctionFragment(it)
                        )
                        viewModel.onDetailNavigated()
                    }
                    getString(R.string.direct_tag) -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalDetailDirectFragment(it)
                        )
                        viewModel.onDetailNavigated()
                    }
                }
            }
        })

        viewModel.navigateDetailChat.observe(viewLifecycleOwner, Observer {
            it?.let {
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

        viewModel.navigateToPost.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalPostFragment())
                viewModel.onPostNavigated()
            }
        })

        viewModel.navigateToCollect.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.imageViewDetailAuctionCollection.visibility = View.GONE
                binding.imageViewDetailAuctionUnCollection.visibility = View.VISIBLE
                findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.COLLECTION_SUCCESS))
                viewModel.onCollectNavigated()
            }
        })

        viewModel.navigateToUnCollect.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.imageViewDetailAuctionCollection.visibility = View.VISIBLE
                binding.imageViewDetailAuctionUnCollection.visibility = View.GONE
                findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.UN_COLLECTION_SUCCESS))
                viewModel.onUnCollectNavigated()
            }
        })

        //最近商品成交價
        viewModel.averageEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getAveragePrice()
            }
        })

        viewModel.navigateToDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.AVERAGE_PRICE))
                viewModel.onDialogNavigated()
            }
        })

        viewModel.timerStart()
        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.timerStop()
    }
}
