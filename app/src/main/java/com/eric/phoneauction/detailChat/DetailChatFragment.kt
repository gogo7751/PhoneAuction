package com.eric.phoneauction.detailChat

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.PhoneAuctionApplication

import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentDetailChatBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailChatFragment : Fragment() {

    val viewModel: DetailChatViewModel by viewModels<DetailChatViewModel> { getVmFactory(DetailChatFragmentArgs.fromBundle(requireArguments()).event) }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO = 1
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (savedInstanceState != null) {
            saveUri = Uri.parse(savedInstanceState.getString("saveUri"))
        }

        permission()

        binding.fabChatDetail.setOnClickListener {
            toAlbum(PHOTO)
        }

        val adapter = DetailChatAdapter(viewModel)

        binding.recyclerviewChatDetail.adapter = adapter

        viewModel.liveMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageChatDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if (viewModel.event.value?.sellerName == UserManager.user.name) {
            binding.textChatTitle.text = UserManager.user.name
        } else {
            binding.textChatTitle.text = viewModel.event.value?.sellerName
        }

        viewModel.image.observe(viewLifecycleOwner, Observer {
            viewModel.message.value?.image = it
            viewModel.message.value?.text = ""
            viewModel.message.value?.let { message -> viewModel.document.value?.let { document ->
                viewModel.postMessage(message, document)
            } }
        })

        binding.imageChatDetailSend.setOnClickListener {
            viewModel.message.value?.let { it1 -> viewModel.document.value?.let { it2 ->
                viewModel.postMessage(it1,
                    it2
                )
            } }
            Handler().postDelayed({binding.editChatDetailInput.text.clear()},500)
        }

        return binding.root
    }

    //上傳圖片
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(saveUri != null){
            val uriString = saveUri.toString()
            outState.putString("saveUri", uriString)
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        val bitmap = MediaStore.Images.Media.getBitmap(PhoneAuctionApplication.instance.contentResolver, saveUri)
                        uploadImage(viewModel.image)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }
        }
    }

    private fun toAlbum(photoFromGallery :Int) {
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
