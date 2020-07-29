package com.eric.phoneauction.searchFragment

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentSearchBinding
import com.eric.phoneauction.dialog.MessageDialog
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.dialog.NoteDialogDirections
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.hideKeyboard
import com.eric.phoneauction.postFragment.PostSpinnerAdapter
import com.eric.phoneauction.util.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels<SearchViewModel> { getVmFactory(SearchFragmentArgs.fromBundle(requireArguments()).search) }
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
                binding.editSearch.hideKeyboard()
                findNavController().navigate(NavigationDirections.actionGlobalSearchFragment(v.text.toString()))
            }
            return@setOnEditorActionListener false
        }

        binding.editSearch.setText(viewModel.search)

        binding.imageSearchClear.setOnClickListener {
            binding.editSearch.text.clear()
        }

        binding.imageSearchNotificationVisibility.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_shakes))

        binding.imageSearchQuestion.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.WISH))
        }

        binding.buttonPost.setOnClickListener {
            viewModel.wishList.value?.let { it1 -> viewModel.postWishList(it1) }
            findNavController().navigate(NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.COLLECTION_SUCCESS))
            Handler().postDelayed({findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())},2500)
        }

        //選擇品牌
        binding.spinnerBrand.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.brand_list)
        )

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    1 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.iphone_list))
                    }
                    2 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.samsung_list))
                    }
                    3 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.sony_list))
                    }
                    4 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.google_list))
                    }
                    5 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.asus_list))
                    }
                    6 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.oppo_list))
                    }
                    7 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.huawei_list))
                    }
                    8 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.mi_list))
                    }
                    9 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.htc_list))
                    }
                    10 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.nokia_list))
                    }
                    11 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.realme_list))
                    }
                    12 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.lg_list))
                    }
                    13 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.sugar_list))
                    }
                    14 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.sharp_list))
                    }
                    15 -> {
                        getSpinnerNameString(PhoneAuctionApplication.instance.resources.getStringArray(R.array.vivo_list))
                    }
                }
                viewModel.wishList.value?.brand = binding.spinnerBrand.selectedItem.toString()
            }
        }

        //選擇名稱
        getSpinnerNameString(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.name_list)
        )

        binding.spinnerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.wishList.value?.productName = binding.spinnerName.selectedItem.toString()
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
                viewModel.wishList.value?.storage = binding.spinnerStorage.selectedItem.toString()
            }
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return binding.root
    }

    //get spinnerNameString
    fun getSpinnerNameString(stringArray: Array<String>) {
        binding.spinnerName.adapter = PostSpinnerAdapter(
            stringArray)
    }

}
