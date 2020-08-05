package com.eric.phoneauction.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DialogImageBinding

class ImageDialog:  AppCompatDialogFragment() {

    var message: String? = null
    private val messageType by lazy {
        ImageDialogArgs.fromBundle(requireArguments()).messageTypeKey
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MessageDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = DialogImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this

        message = messageType

        return binding.root
    }

    interface IMessage {
        var message: String
    }

    class Message : IMessage {
        private var _message = ""
        override var message: String
            get() = _message
            set(value) { _message = value }
    }
}