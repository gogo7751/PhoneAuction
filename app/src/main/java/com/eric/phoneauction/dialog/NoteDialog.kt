package com.eric.phoneauction.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.eric.phoneauction.PhoneAuctionApplication

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DialogNoteBinding

/**
 * A simple [Fragment] subclass.
 */
class NoteDialog : AppCompatDialogFragment() {

    var iconRes: Drawable? = null
    var message: String? = null

    private val messageType by lazy {
        NoteDialogArgs.fromBundle(requireArguments()).messageTypeKey
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.NoteDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        val binding = DialogNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this

        return binding.root
    }

    private fun init() {
        when (messageType) {
            MessageType.AVERAGE_PRICE -> {
                iconRes = PhoneAuctionApplication.instance.getDrawable(R.drawable.ic_questionmark_white)
                message = getString(R.string.note_average_price)
            }
            MessageType.NO_AVERAGE_PRICE -> {
                iconRes = PhoneAuctionApplication.instance.getDrawable(R.drawable.ic_questionmark)
                message = getString(R.string.un_collection_success)
            }
            MessageType.WISH -> {
                iconRes = PhoneAuctionApplication.instance.getDrawable(R.drawable.ic_questionmark_white)
                message = getString(R.string.note_wish)
            }
        }
    }

    enum class MessageType(val value: MessageDialog.Message) {
        AVERAGE_PRICE(MessageDialog.Message()),
        NO_AVERAGE_PRICE(MessageDialog.Message()),
        WISH(MessageDialog.Message())
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
