package com.base.structure.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.structure.R
import com.base.structure.base.BaseActivity
import com.base.structure.databinding.LoginBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.mPrefs
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class SignInActivity : BaseActivity() {


    private val TAG = "Activity"
    lateinit var loginBinding: LoginBinding

    @Inject
     lateinit var userdetailFactory: ViewModelFactory
     lateinit var mUserViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        hideKeyBoard()
        setObservers()


        loginBinding.resetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBinding.signIn.setOnClickListener {

            if(isValid()){
                callUserLogin(loginBinding.signInMobile.text.toString(),loginBinding.signInPassword.text.toString())
            }
        }

    }

    private fun isValid(): Boolean {

        if (!loginBinding.signInMobile.text.matches("[0-9]{10}".toRegex())) {
            loginBinding.signInMobile.error = "please check the Mobile no."
            loginBinding.signInMobile.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(loginBinding.signInPassword.text.toString().trim())) {
            loginBinding.signInPassword.error = "please check the Username"
            loginBinding.signInPassword.requestFocus()
            return false
        }

        return true
    }



        fun callUserLogin( mobile: String, password: String) {

            if (!ConnectivityStatus.isConnected(this)) {
                showNetworkIssue()
                return
            }

            mUserViewModel?.userLogin(mobile, password)
        }

    override fun retryAction() {
        super.retryAction()

    }

    fun observeLogin(){
        mUserViewModel?.getLoginLiveData()?.observe(this, Observer { resource ->

            mPrefs?.prefUserDetails = resource!!
            val intent = Intent(this, HomeActivity::class.java)
            //   intent.putExtra("phone", phoneno)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("userId", mPrefs?.prefUserDetails!!.id)

            startActivity(intent)
            finish()
            showToast("success")
        })

    }

    private fun setObservers(){
        observeExtras()
        observeLogin()
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



