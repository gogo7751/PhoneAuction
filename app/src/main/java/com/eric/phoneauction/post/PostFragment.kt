package com.eric.phoneauction.post

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentPostBinding
import com.eric.phoneauction.dialog.NoteDialog
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.hideKeyboard
import com.eric.phoneauction.ext.permission
import com.eric.phoneauction.util.Logger
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class PostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel> { getVmFactory() }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO_FROM_GALLERY_1 = 1
        const val PHOTO_FROM_GALLERY_2 = 2
        const val PHOTO_FROM_GALLERY_3 = 3
        const val PHOTO_FROM_GALLERY_4 = 4
        const val PHOTO_FROM_GALLERY_5 = 5
    }

    lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        if (savedInstanceState != null) {
            saveUri = Uri.parse(savedInstanceState.getString("saveUri"))
        }

        permission()

        binding.imagePost1.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY_1)
        }

        binding.imagePost2.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY_2)
        }

        binding.imagePost3.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY_3)
        }

        binding.imagePost4.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY_4)
        }

        binding.imagePost5.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY_5)
        }


        viewModel.wishList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.userId.isEmpty()) {
                    Logger.d("")
                } else {
                    viewModel.postNotification(viewModel.getNotification(getString(R.string.wish_list)), it.userId)
                }
            }
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let { needRefresh ->
                if (needRefresh) {
                    ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
                        refresh()
                    }
                }
                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })


        binding.editPostDescription.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.editPostDescription.hideKeyboard()
            }
            return@setOnEditorActionListener false
        }

        binding.editPostAuction.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.editPostAuction.hideKeyboard()
            }
            return@setOnEditorActionListener false
        }

        binding.editPostDirect.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.editPostDirect.hideKeyboard()
            }
            return@setOnEditorActionListener false
        }

        //選擇品牌
        binding.spinnerBrand.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.brand_list)
        )

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    1 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.iphone_list
                            )
                        )
                    }
                    2 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.samsung_list
                            )
                        )
                    }
                    3 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.sony_list
                            )
                        )
                    }
                    4 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.google_list
                            )
                        )
                    }
                    5 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.asus_list
                            )
                        )
                    }
                    6 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.oppo_list
                            )
                        )
                    }
                    7 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.huawei_list
                            )
                        )
                    }
                    8 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.mi_list
                            )
                        )
                    }
                    9 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.htc_list
                            )
                        )
                    }
                    10 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.nokia_list
                            )
                        )
                    }
                    11 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.realme_list
                            )
                        )
                    }
                    12 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.lg_list
                            )
                        )
                    }
                    13 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.sugar_list
                            )
                        )
                    }
                    14 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.sharp_list
                            )
                        )
                    }
                    15 -> {
                        getSpinnerNameString(
                            PhoneAuctionApplication.instance.resources.getStringArray(
                                R.array.vivo_list
                            )
                        )
                    }
                }
                viewModel.brand.value = binding.spinnerBrand.selectedItem.toString()
            }
        }

        //選擇名稱
        getSpinnerNameString(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.name_list)
        )

        binding.spinnerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.productName.value = binding.spinnerName.selectedItem.toString()
            }
        }


        //選擇交易方式
        binding.spinnerTrade.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.trade_list)
        )

        binding.spinnerTrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.trade.value = binding.spinnerTrade.selectedItem.toString()
            }
        }

        //選擇拍賣方式
        binding.spinnerTag.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.tag_list)
        )
        binding.spinnerTag.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    1 -> {
                        binding.editPostDirect.visibility = View.VISIBLE
                        binding.editPostAuction.visibility = View.GONE
                        binding.imagePostQuestion.visibility = View.VISIBLE
                        binding.textPostAverage.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.editPostDirect.visibility = View.GONE
                        binding.editPostAuction.visibility = View.VISIBLE
                        binding.imagePostQuestion.visibility = View.VISIBLE
                        binding.textPostAverage.visibility = View.VISIBLE
                    }
                }
                viewModel.tag.value = binding.spinnerTag.selectedItem.toString()
                viewModel.getAveragePriceResult(
                    viewModel.brand.value.toString(),
                    viewModel.productName.value.toString(),
                    viewModel.storage.value.toString(),
                    false
                )
            }
        }


        //最近商品成交價
        viewModel.events.observe(viewLifecycleOwner, Observer { list ->
            list?.let { event ->
                viewModel.averagePrice.value = event.map { it.price }.average().toInt()
            }
        })

        binding.imagePostQuestion.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalNoteDialog(NoteDialog.MessageType.AVERAGE_PRICE))
        }


        //選擇容量
        binding.spinnerStorage.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.storage_list)
        )

        binding.spinnerStorage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    viewModel.storage.value = binding.spinnerStorage.selectedItem.toString()
                }
            }


        //返回上一頁
        binding.imageViewPostBack.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE

        return binding.root
    }

    //get spinnerNameString
    fun getSpinnerNameString(stringArray: Array<String>) {
        binding.spinnerName.adapter = PostSpinnerAdapter(
            stringArray
        )
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
    }

    //上傳圖片
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveUri != null) {
            val uriString = saveUri.toString()
            outState.putString("saveUri", uriString)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO_FROM_GALLERY_1 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            PhoneAuctionApplication.instance.contentResolver,
                            saveUri
                        )
                        binding.imagePost1.setImageBitmap(bitmap)
                        uploadImage(viewModel.image1)
                    }
                    Activity.RESULT_CANCELED -> {
                        Logger.d(resultCode.toString())
                    }
                }
            }
            PHOTO_FROM_GALLERY_2 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            PhoneAuctionApplication.instance.contentResolver,
                            saveUri
                        )
                        binding.imagePost2.setImageBitmap(bitmap)
                        uploadImage(viewModel.image2)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
            PHOTO_FROM_GALLERY_3 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            PhoneAuctionApplication.instance.contentResolver,
                            saveUri
                        )
                        binding.imagePost3.setImageBitmap(bitmap)
                        uploadImage(viewModel.image3)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
            PHOTO_FROM_GALLERY_4 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            PhoneAuctionApplication.instance.contentResolver,
                            saveUri
                        )
                        binding.imagePost4.setImageBitmap(bitmap)
                        uploadImage(viewModel.image4)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
            PHOTO_FROM_GALLERY_5 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            PhoneAuctionApplication.instance.contentResolver,
                            saveUri
                        )
                        binding.imagePost5.setImageBitmap(bitmap)
                        uploadImage(viewModel.image5)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
        }
    }

    private fun toAlbum(photoFromGallery: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, photoFromGallery)
    }

    private fun uploadImage(image: MutableLiveData<String>) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        saveUri?.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        image.value = it.toString()
                    }
                }
        }
    }
}