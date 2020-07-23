package com.eric.phoneauction.searchFragment

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentSearchBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.util.Logger

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels<SearchViewModel> { getVmFactory(SearchFragmentArgs.fromBundle(requireArguments()).search) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = SearchAdapter(SearchAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        }, viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerviewSearch.adapter = adapter

        viewModel.liveSearchs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageSearchBack.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
        }

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

        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                findNavController().navigate(NavigationDirections.actionGlobalSearchFragment(v.text.toString()))
            }
            return@setOnEditorActionListener false
        }

        binding.editSearch.setText(viewModel.search)

        binding.imageSearchClear.setOnClickListener {
            binding.editSearch.text.clear()
        }



        return binding.root
    }

}
