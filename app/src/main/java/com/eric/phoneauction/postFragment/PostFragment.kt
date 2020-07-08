package com.eric.phoneauction.postFragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.PostFragmentBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class PostFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel> { getVmFactory() }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO_FROM_GALLERY_1 = 0
        const val PHOTO_FROM_GALLERY_2 = 1
        const val PHOTO_FROM_GALLERY_3 = 2
        const val PHOTO_FROM_GALLERY_4 = 3
        const val PHOTO_FROM_GALLERY_5 = 4
    }

    lateinit var binding: PostFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostFragmentBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            saveUri = Uri.parse(savedInstanceState.getString("saveUri"))
        }

        permission()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.imagePost1.setOnClickListener {
            toAlbum1()
            Handler().postDelayed({
                uploadImage1()
            }, 5000)
        }

        binding.imagePost2.setOnClickListener {
            toAlbum2()
            Handler().postDelayed({
                uploadImage2()
            }, 5000)
        }

        binding.imagePost3.setOnClickListener {
            toAlbum3()
            Handler().postDelayed({
                uploadImage3()
            }, 5000)
        }

        binding.imagePost4.setOnClickListener {
            toAlbum4()
            Handler().postDelayed({
                uploadImage4()
            }, 5000)
        }

        binding.imagePost5.setOnClickListener {
            toAlbum5()
            Handler().postDelayed({
                uploadImage5()
            }, 5000)
        }

        //送出post
        binding.buttonPost.setOnClickListener {
            viewModel.post(viewModel.getEvent())
        }

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
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.iphone_list)) }
                    2 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.samsung_list)) }
                    3 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.sony_list)) }
                    4 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.google_list)) }
                    5 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.asus_list)) }
                    6 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.oppo_list)) }
                    7 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.huawei_list)) }
                    8 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.mi_list)) }
                    9 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.htc_list)) }
                    10 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.nokia_list)) }
                    11 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.realme_list)) }
                    12 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.lg_list)) }
                    13 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.sugar_list)) }
                    14 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.sharp_list)) }
                    15 -> {
                        binding.spinnerName.adapter = PostSpinnerAdapter(
                            PhoneAuctionApplication.instance.resources.getStringArray(R.array.vivo_list)) }
                }
                viewModel.brand.value = binding.spinnerBrand.selectedItem.toString()
            }
        }

        //選擇名稱
        binding.spinnerName.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.name_list)
        )

        binding.spinnerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.productName.value = binding.spinnerName.selectedItem.toString()
            }
        }


        //選擇交易方式
        binding.spinnerTrade.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.trade_list)
        )

        binding.spinnerTrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.trade.value = binding.spinnerTrade.selectedItem.toString()
            }
        }

        //選擇拍賣方式
        binding.spinnerTag.adapter = PostSpinnerAdapter(
            PhoneAuctionApplication.instance.resources.getStringArray(R.array.tag_list)
        )
        binding.spinnerTag.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    1 -> {
                        binding.editPostDirect.visibility = View.VISIBLE
                        binding.editPostAuction.visibility = View.GONE
                    }
                    2 -> {
                        binding.editPostDirect.visibility = View.GONE
                        binding.editPostAuction.visibility = View.VISIBLE
                    }
                }
                viewModel.tag.value = binding.spinnerTag.selectedItem.toString()
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
                viewModel.storage.value = binding.spinnerStorage.selectedItem.toString()
            }
        }


        //返回上一頁
        binding.imageViewPostBack.setOnClickListener {
            findNavController().navigateUp()
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE

        return binding.root
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
    }

    //上傳圖片
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(saveUri != null){
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
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        binding.imagePost1.setImageBitmap(bitmap)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
            PHOTO_FROM_GALLERY_2 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        binding.imagePost2.setImageBitmap(bitmap)
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
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        binding.imagePost3.setImageBitmap(bitmap)
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
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        binding.imagePost4.setImageBitmap(bitmap)
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
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        binding.imagePost5.setImageBitmap(bitmap)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
        }
    }

    private fun toAlbum1() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY_1)
    }

    private fun toAlbum2() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY_2)
    }

    private fun toAlbum3() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY_3)
    }

    private fun toAlbum4() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY_4)
    }

    private fun toAlbum5() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_FROM_GALLERY_5)
    }

    private fun permission() {
        val permissionList = arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        var size = permissionList.size
        var i = 0
        while (i < size) {
            if (ActivityCompat.checkSelfPermission(PhoneAuctionApplication.instance.applicationContext, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
                permissionList.removeAt(i)
                i -= 1
                size -= 1
            }
            i += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions((activity as AppCompatActivity), permissionList.toArray(array), 0)
    }


    private fun uploadImage1() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(saveUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    viewModel.image1.value = it.toString()
                }
            }
    }

    private fun uploadImage2() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(saveUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    viewModel.image2.value = it.toString()
                }
            }
    }

    private fun uploadImage3() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(saveUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    viewModel.image3.value = it.toString()
                }
            }
    }

    private fun uploadImage4() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(saveUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    viewModel.image4.value = it.toString()
                }
            }
    }

    private fun uploadImage5() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(saveUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    viewModel.image5.value = it.toString()
                }
            }
    }
}
