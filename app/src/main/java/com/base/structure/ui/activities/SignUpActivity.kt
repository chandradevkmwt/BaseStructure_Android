package com.base.structure.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.structure.R
import com.base.structure.base.BaseActivity
import com.base.structure.databinding.SignUpBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject


class SignUpActivity : BaseActivity() {

    private val TAG = "Activity"
    lateinit var signUpBinding: SignUpBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        hideKeyBoard()
        setObservers()

        signUpBinding.signInHere.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        })

        signUpBinding.signUp.setOnClickListener(View.OnClickListener {

            if(isValid()){
                callUserSignUp(signUpBinding.signUpMobile.text.toString(),signUpBinding.signUpPassword.text.toString())
            }

        })



    }


    private fun isValid(): Boolean {

        if (!signUpBinding.signUpMobile.text.matches("[0-9]{10}".toRegex())) {
            signUpBinding.signUpMobile.error = "please check the Mobile no."
            signUpBinding.signUpMobile.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(signUpBinding.signUpPassword.text.toString().trim())) {
            signUpBinding.signUpPassword.error = "please check the Username"
            signUpBinding.signUpPassword.requestFocus()
            return false
        }

        return true
    }



    fun callUserSignUp( mobile: String, password: String) {

        if (!ConnectivityStatus.isConnected(this)) {
            showNetworkIssue()
            return
        }

        mUserViewModel?.signUp(mobile, password)
    }


    fun observeSignUp(){
        mUserViewModel?.getSignUpLiveData()?.observe(this, Observer { resource ->

//            mPrefs?.prefUserDetails = resource!!

            val intent = Intent(this, OtpActivity::class.java)
            //   intent.putExtra("phone", phoneno)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            showToast("success")
        })

    }

    private fun setObservers(){
        observeExtras()
        observeSignUp()
    }


    private fun observeExtras() {
        mUserViewModel!!.loading.observe(this,
            { isLoading -> handleProgressLoader(isLoading!!) })
        mUserViewModel!!.dataLoadError.observe(this, { s ->
            Log.e(TAG, "onChanged: $s")
            handleError(s)
        })
    }
}