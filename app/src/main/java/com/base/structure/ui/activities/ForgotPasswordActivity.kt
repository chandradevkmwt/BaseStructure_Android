package com.base.structure.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.structure.R
import com.base.structure.base.BaseActivity
import com.base.structure.databinding.ForgotPasswordBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity() {

    private val TAG = "Activity"
    lateinit var forgotBinding: ForgotPasswordBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        forgotBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        hideKeyBoard()
        setObservers()

        forgotBinding.reset.setOnClickListener(View.OnClickListener {

            if(isValid()){
                callForgotPassword(forgotBinding.forgotPassMobile.text.toString())
            }

        })

    }


    private fun isValid(): Boolean {

        if (!forgotBinding.forgotPassMobile.text.matches("[0-9]{10}".toRegex())) {
            forgotBinding.forgotPassMobile.error = "please check the Mobile no."
            forgotBinding.forgotPassMobile.requestFocus()
            return false
        }

        return true
    }



    fun callForgotPassword( number: String) {

        if (!ConnectivityStatus.isConnected(this)) {
            showNetworkIssue()
            return
        }

        mUserViewModel?.forgotPassword(number)
    }


    fun observeSignUp(){
        mUserViewModel?.getForgotPasswordLiveData()?.observe(this, Observer { resource ->

//            mPrefs?.prefUserDetails = resource!!

            val intent = Intent(this, SignInActivity::class.java)
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