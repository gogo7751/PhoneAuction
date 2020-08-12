package com.eric.phoneauction.home

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentHomeBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.hideKeyboard
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(
            inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerviewHome.adapter = adapter

        viewModel.refreshStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.layoutSwipeRefreshHomepage.isRefreshing = it
            }
        })

        binding.layoutSwipeRefreshHomepage.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.tag) {
                    getString(R.string.auction_tag) -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailAuctionFragment(it))
                        viewModel.onDetailNavigated()
                    }
                    getString(R.string.direct_tag) -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDetailDirectFragment(it))
                        viewModel.onDetailNavigated()
                    }
                }
            }
        })

        binding.spinnerSort.adapter = HomeSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.sort_list)
        )

        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) ) {
                binding.editSearch.hideKeyboard()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment(v.text.toString()))
            }
            return@setOnEditorActionListener false
        }

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        viewModel.getEventsResult()
                        viewModel.getAllSort()
                    }
                    1 -> {
                        when {
                            viewModel.isAuction.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.auction_tag), getString(R.string.sort_price), Query.Direction.DESCENDING)
                            }
                            viewModel.isDirect.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.direct_tag), getString(R.string.sort_price), Query.Direction.DESCENDING)
                            }
                            else -> {
                                viewModel.getSort(getString(R.string.sort_price), Query.Direction.DESCENDING)
                            }
                        }
                    }
                    2 -> {
                        when {
                            viewModel.isAuction.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.auction_tag), getString(R.string.sort_price), Query.Direction.ASCENDING)
                            }
                            viewModel.isDirect.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.direct_tag), getString(R.string.sort_price), Query.Direction.ASCENDING)
                            }
                            else -> {
                                viewModel.getSort(getString(R.string.sort_price), Query.Direction.ASCENDING)
                            }
                        }
                    }
                    3 -> {
                        when {
                            viewModel.isAuction.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.auction_tag), getString(R.string.sort_endTime), Query.Direction.DESCENDING)
                            }
                            viewModel.isDirect.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.direct_tag), getString(R.string.sort_endTime), Query.Direction.DESCENDING)
                            }
                            else -> {
                                viewModel.getSort(getString(R.string.sort_endTime), Query.Direction.DESCENDING)
                            }
                        }
                    }
                    4 -> {
                        when {
                            viewModel.isAuction.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.auction_tag), getString(R.string.sort_endTime), Query.Direction.ASCENDING)
                            }
                            viewModel.isDirect.value == true -> {
                                viewModel.getSortWithTagResult(getString(R.string.direct_tag), getString(R.string.sort_endTime), Query.Direction.ASCENDING)
                            }
                            else -> {
                                viewModel.getSort(getString(R.string.sort_endTime), Query.Direction.ASCENDING)
                            }
                        }
                    }
                }
            }
        }

        viewModel.isAuction.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.spinnerSort.setSelection(0)
            }
        })

        viewModel.isDirect.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.spinnerSort.setSelection(0)
            }
        })

        binding.imageHomeClear.setOnClickListener {
            binding.editSearch.text.clear()
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }
}
