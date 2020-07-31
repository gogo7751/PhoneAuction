package com.eric.phoneauction.chatToChatDetailFragment

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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.chatFragment.ChatViewModel
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentChatToDetailChatBinding
import com.eric.phoneauction.databinding.FragmentDetailChatBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.postFragment.PostFragment
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatToDetailChatFragment : Fragment() {

    val viewModel: ChatToDetailChatViewModel by viewModels<ChatToDetailChatViewModel> { getVmFactory(ChatToDetailChatFragmentArgs.fromBundle(requireArguments()).chatRoom) }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO_FROM_GALLERY = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatToDetailChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (savedInstanceState != null) {
            saveUri = Uri.parse(savedInstanceState.getString("saveUri"))
        }

        permission()

        binding.fabChatToDetail.setOnClickListener {
            toAlbum(PHOTO_FROM_GALLERY)
        }

        val adapter = ChatToDetailChatAdapter(viewModel)
        binding.recyclerviewChatToDetail.adapter = adapter

        viewModel.liveMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageChatToDetailBack.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalChatFragment())
        }

        if (viewModel.chatRoom.value?.senderId == UserManager.userId) {
            binding.textChatToDetailTitle.text = viewModel.chatRoom.value?.receiverName
        } else {
            binding.textChatToDetailTitle.text = viewModel.chatRoom.value?.senderName
        }

        viewModel.image.observe(viewLifecycleOwner, Observer {
            viewModel.message.value?.image = it
            viewModel.message.value?.text = ""
            viewModel.message.value?.let { message -> viewModel.document.value?.let { document ->
                viewModel.postMessage(message, document)
            } }
        })


        binding.imageChatToDetailSend.setOnClickListener {
            viewModel.message.value?.let { message -> viewModel.document.value?.let { document ->
                viewModel.postMessage(message, document)
            } }
            Handler().postDelayed({binding.editChatToDetailInput.text.clear()},500)
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
            PHOTO_FROM_GALLERY -> {
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


