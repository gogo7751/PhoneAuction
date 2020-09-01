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
import com.eric.phoneauction.ext.permission
import com.eric.phoneauction.util.Logger
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailChatFragment : Fragment() {

    val viewModel: DetailChatViewModel by viewModels {
        getVmFactory(DetailChatFragmentArgs.fromBundle(requireArguments()).event) }

    private var saveUri: Uri? = null

    private companion object {
        const val PHOTO = 1
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailChatBinding.inflate(inflater,
            container, false)

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

        viewModel.sendImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.setImageValue(it)
            }
        })

        viewModel.setEditText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editChatDetailInput.text.clear()
                viewModel.clearEditText()
            }
        })

        return binding.root
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
            PHOTO -> {
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
