package com.eric.phoneauction.profilePolicy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainActivity
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentProfilePolicyBinding
import com.eric.phoneauction.util.Logger
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.android.synthetic.main.activity_main.*

class ProfilePolicyFragment : Fragment() {

    private var reviewInfo: ReviewInfo? = null
    private lateinit var reviewManager: ReviewManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewBinding = FragmentProfilePolicyBinding.inflate(inflater, container, false)

        viewBinding.webView.loadUrl(getString(R.string.privacy_policy))
        viewBinding.webView.loadUrl("https://docs.google.com/document/d/1fS0I6PN980hEX1K5jTg5cjInUaPvNTub05IfiJVsXok/edit#")
        viewBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                viewBinding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewBinding.progressBar.visibility = View.GONE
            }
        }

        reviewManager = ReviewManagerFactory.create(context)

        val requestFlow = reviewManager.requestReviewFlow()
        requestFlow.addOnCompleteListener { request ->
            reviewInfo = if (request.isSuccessful) {
                //Received ReviewInfo object
                request.result
            } else {
                //Problem in receiving object
                null
            }
        }

        fun inAppReview() {
            if (reviewInfo != null) {
                reviewManager.launchReviewFlow(activity, reviewInfo).addOnFailureListener {
                    findNavController().navigateUp()
                    Logger.d("fail:$it")
                    // Log error and continue with the flow
                }.addOnCompleteListener { _ ->
                    // Log success and continue with the flow
                    findNavController().navigateUp()
                    Logger.d("success:flow")
                }
            }
        }

        viewBinding.imagePolicyBack.setOnClickListener {
            inAppReview()
        }

//        viewBinding.imagePolicyBack.setOnClickListener {
//            findNavController().navigateUp()
//        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE
        return viewBinding.root
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == ACTIVITY_CALLBACK && resultCode == Activity.RESULT_OK) {
//            Handler().postDelayed({
//                reviewInfo?.let { it ->
//                    val flow = reviewManager.launchReviewFlow(activity, it)
//                    flow.addOnSuccessListener {
//                        //Showing toast is only for testing purpose, this shouldn't be implemented
//                        //in production app.
//                        Toast.makeText(context, "Thanks for the feedback!", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                    flow.addOnFailureListener {
//                        //Showing toast is only for testing purpose, this shouldn't be implemented
//                        //in production app.
//                        Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
//                    }
//                    flow.addOnCompleteListener {
//                        //Showing toast is only for testing purpose, this shouldn't be implemented
//                        //in production app.
//                        Toast.makeText(context, "Completed!", Toast.LENGTH_LONG).show()
//                    }
//                }
//            }, 3000)
//        }
//    }


}

