package com.base.structure.ui.fragments

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.base.structure.base.BaseFragment
import com.base.structure.databinding.ChangePasswordBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.ui.activities.SignInActivity
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ChangePasswordFragment : BaseFragment() {

    lateinit var changePasswordBinding:ChangePasswordBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        changePasswordBinding = ChangePasswordBinding.inflate(inflater, container, false)

        hideKeyBoard()
        setObservers()

        changePasswordBinding.changePassword.setOnClickListener(View.OnClickListener {

            if (isValid()){

                changePassword(changePasswordBinding.changePassMobile.text.toString(),
                    changePasswordBinding.currentPassword.text.toString(),changePasswordBinding.newPassword.text.toString())

            }
        })

        val root: View = changePasswordBinding.root
        return root
    }


    fun changePassword(mobile:String, password:String, newPassword:String){

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

        mUserViewModel.getChangePassword(mobile, password, newPassword)

    }

    private fun isValid(): Boolean {

        if (!changePasswordBinding.changePassMobile.text.matches("[0-9]{10}".toRegex())) {
            changePasswordBinding.changePassMobile.error = "please check the Mobile no."
            changePasswordBinding.changePassMobile.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(changePasswordBinding.currentPassword.text.toString().trim())) {
            changePasswordBinding.currentPassword.error = "please check the Username"
            changePasswordBinding.currentPassword.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(changePasswordBinding.newPassword.text.toString().trim())) {
            changePasswordBinding.newPassword.error = "please check the Username"
            changePasswordBinding.newPassword.requestFocus()
            return false
        }

        return true
    }


    fun observeHome(){

        mUserViewModel.getChangePasswordLiveData().observe(viewLifecycleOwner, Observer {

            if (it == true){

                val intent = Intent(activity, SignInActivity::class.java)

                startActivity(intent)
                activity.finish()
            }

        })


    }





    private fun setObservers(){

        observeExtras()
        observeHome()

    }


    private fun observeExtras() {
        mUserViewModel!!.loading.observe(activity,
            { isLoading -> handleProgressLoader(isLoading!!) })
        mUserViewModel!!.dataLoadError.observe(activity, { s ->
            Log.e(TAG, "onChanged: $s")
            handleError(s)
        })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}