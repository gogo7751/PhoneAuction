package com.eric.phoneauction

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.databinding.ActivityLoginBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.util.Logger.d
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private lateinit var binding: ActivityLoginBinding

    private var auth : FirebaseAuth? = null
    private var callbackManager : CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        FacebookSdk.sdkInitialize(this)

        auth = FirebaseAuth.getInstance()

        binding.facebookLoginButton.setOnClickListener {
            //First step
            facebookLogin()
        }

        viewModel.status.observe(this, androidx.lifecycle.Observer {
            it?.let {
                when(it) {
                    LoadApiStatus.LOADING -> {
                        binding.progressBarLogin.visibility = View.VISIBLE
                        binding.viewLoadingBg.visibility = View.VISIBLE
                    }
                    LoadApiStatus.DONE -> moveMainPage(auth?.currentUser)
                    else -> d(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing))
                }
            }
        })

        callbackManager = CallbackManager.Factory.create()

    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }


    fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult>{

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }

                override fun onSuccess(result: LoginResult) {
                    viewModel.handleFacebookAccessToken(result?.accessToken)
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)
    }

    fun moveMainPage(user:FirebaseUser?){
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

