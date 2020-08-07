package com.eric.phoneauction.search

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentSearchBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.hideKeyboard
import com.eric.phoneauction.post.PostSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels {
        getVmFactory(SearchFragmentArgs.fromBundle(requireArguments()).search) }
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
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

        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.editSearch.hideKeyboard()
                findNavController().navigate(NavigationDirections.actionGlobalSearchFragment(v.text.toString()))
            }
            return@setOnEditorActionListener false
        }

        binding.imageSearchClear.setOnClickListener {
            binding.editSearch.text.clear()
        }

        binding.imageSearchNotificationVisibility.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.anim_shakes)
        )

        viewModel.navigateToDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.WISH))
                viewModel.onDialogNavigated()
            }
        })

        viewModel.navigateToCollect.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.COLLECTION_SUCCESS))
                Handler().postDelayed({findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())},2500)
                viewModel.onCollectNavigated()
            }
        })

        //選擇品牌
        binding.spinnerBrand.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.brand_list)
        )

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                getSpinnerNameString (
                    when (position) {
                        1 -> R.array.iphone_list
                        2 -> R.array.samsung_list
                        3 -> R.array.sony_list
                        4 -> R.array.google_list
                        5 -> R.array.asus_list
                        6 -> R.array.oppo_list
                        7 -> R.array.huawei_list
                        8 -> R.array.mi_list
                        9 -> R.array.htc_list
                        10 -> R.array.nokia_list
                        11 -> R.array.realme_list
                        12 -> R.array.lg_list
                        13 -> R.array.sugar_list
                        14 -> R.array.sharp_list
                        15 -> R.array.vivo_list
                        else -> R.array.empty_list
                    })
                viewModel.getBrandValue(binding.spinnerBrand.selectedItem.toString())
            }
        }

        //選擇名稱
        getSpinnerNameString(R.array.name_list)
        binding.spinnerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.getProductNameValue(binding.spinnerName.selectedItem.toString())
            }
        }

        //選擇容量
        binding.spinnerStorage.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.storage_list)
        )

        binding.spinnerStorage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.getStorageValue(binding.spinnerStorage.selectedItem.toString())
            }
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return binding.root
    }

    //get spinnerNameString
    fun getSpinnerNameString(stringArray: Int) {
        binding.spinnerName.adapter = PostSpinnerAdapter(PhoneAuctionApplication.instance.resources.getStringArray(stringArray))
    }
}
