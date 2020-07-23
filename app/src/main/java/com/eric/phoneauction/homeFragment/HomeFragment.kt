package com.eric.phoneauction.homeFragment

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.HomeFragmentBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.toDisplayFormat
import com.eric.phoneauction.util.Logger
import com.google.firebase.firestore.Query
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
        adapter.setHasStableIds(true)
        binding.recyclerviewHome.adapter = adapter


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

        binding.spinnerSort.adapter = HomeSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.sort_list)
        )

        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
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
                        binding.spinnerSort.setBackgroundResource(R.drawable.spinner_home_bg)
                    }
                    1 -> {
                        if (viewModel.isAuction.value == true) {
                            viewModel.getSortWithTagResult("拍賣", "price", Query.Direction.DESCENDING)
                        } else if (viewModel.isDirect.value == true) {
                            viewModel.getSortWithTagResult("直購", "price", Query.Direction.DESCENDING)
                        } else {
                            viewModel.getSort("price", Query.Direction.DESCENDING)
                        }
                        binding.spinnerSort.setBackgroundResource(R.drawable.spinner_home_selected)
                    }
                    2 -> {
                        if (viewModel.isAuction.value == true) {
                            viewModel.getSortWithTagResult("拍賣", "price", Query.Direction.ASCENDING)
                        } else if (viewModel.isDirect.value == true) {
                            viewModel.getSortWithTagResult("直購", "price", Query.Direction.ASCENDING)
                        } else {
                            viewModel.getSort("price", Query.Direction.ASCENDING)
                        }
                        binding.spinnerSort.setBackgroundResource(R.drawable.spinner_home_selected)
                    }
                    3 -> {
                        if (viewModel.isAuction.value == true) {
                            viewModel.getSortWithTagResult("拍賣", "endTime", Query.Direction.DESCENDING)
                        } else if (viewModel.isDirect.value == true) {
                            viewModel.getSortWithTagResult("直購", "endTime", Query.Direction.DESCENDING)
                        } else {
                            viewModel.getSort("endTime", Query.Direction.DESCENDING)
                        }
                        binding.spinnerSort.setBackgroundResource(R.drawable.spinner_home_selected)
                    }
                    4 -> {
                        if (viewModel.isAuction.value == true) {
                            viewModel.getSortWithTagResult("拍賣", "endTime", Query.Direction.ASCENDING)
                        } else if (viewModel.isDirect.value == true) {
                            viewModel.getSortWithTagResult("直購", "endTime", Query.Direction.ASCENDING)
                        } else {
                            viewModel.getSort("endTime", Query.Direction.ASCENDING)
                        }
                        binding.spinnerSort.setBackgroundResource(R.drawable.spinner_home_selected)
                    }
                }
            }
        }

        binding.buttonAuction.setOnClickListener {
            viewModel.isAuction.value = true
            viewModel.isAuction.value = viewModel.isAuction.value
            viewModel.isDirect.value = false
            viewModel.isDirect.value = viewModel.isDirect.value
            binding.spinnerSort.setSelection(0)
            viewModel.getAuctionResult()
        }

        binding.buttonDirect.setOnClickListener {
            viewModel.isDirect.value = true
            viewModel.isDirect.value = viewModel.isDirect.value
            viewModel.isAuction.value = false
            viewModel.isAuction.value = viewModel.isAuction.value
            binding.spinnerSort.setSelection(0)
            viewModel.getDirectResult()
        }

        binding.imageHomeClear.setOnClickListener {
            binding.editSearch.text.clear()
        }


        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }


}
