package com.eric.phoneauction.chatToChatDetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.databinding.FragmentChatToDetailChatBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.permission
import com.eric.phoneauction.util.Logger
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatToDetailChatFragment : Fragment() {

    val viewModel: ChatToDetailChatViewModel by viewModels {
        getVmFactory(ChatToDetailChatFragmentArgs.fromBundle(requireArguments()).chatRoom) }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO_FROM_GALLERY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatToDetailChatBinding.inflate(inflater,
            container, false)
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

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalChatFragment())
                viewModel.onLeaveCompleted()
            }
        })

        viewModel.sendImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.sendImage(it)
            }
        })

        viewModel.setEditText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editChatToDetailInput.text.clear()
                viewModel.clearEditText()
            }
        })

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
        if (saveUri != null) {
            val uriString = saveUri.toString()
            outState.putString("saveUri", uriString)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO_FROM_GALLERY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        saveUri = data?.data
                        saveUri?.let { viewModel.uploadImage(it) }
                    }
                    Activity.RESULT_CANCELED -> {
                        Logger.d(resultCode.toString())
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
}


