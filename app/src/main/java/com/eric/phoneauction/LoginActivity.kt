package com.eric.phoneauction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.util.Logger
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.firestore.auth.User

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var callbackManager : CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        facebook_login_button.setOnClickListener {
            //First step
            facebookLogin()
        }

        callbackManager = CallbackManager.Factory.create()
//        Handler().postDelayed({lottie_login_title.visibility = View.GONE},4000)
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }




    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }

    }

    fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    //Second step
                    handleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }

    fun handleFacebookAccessToken(token : AccessToken?){
        var credential = token?.token?.let { FacebookAuthProvider.getCredential(it) }
        if (credential != null) {
            auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){
                        //Third step
                        //Login
                        moveMainPage(task.result?.user)
                        val user = com.eric.phoneauction.data.User(
                            id = task.result?.user?.uid.toString(),
                            image = task.result?.user?.photoUrl.toString(),
                            name = task.result?.user?.displayName.toString()
                        )
                        UserManager.userId = task.result?.user?.uid.toString()
                        UserManager.user = user

                    }else{
                        //Show the error message
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)
    }


    fun moveMainPage(user:FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

