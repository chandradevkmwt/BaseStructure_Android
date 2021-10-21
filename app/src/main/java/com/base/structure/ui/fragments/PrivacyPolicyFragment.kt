package com.base.structure.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.base.structure.base.BaseFragment
import com.base.structure.databinding.PrivacyPolicyBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PrivacyPolicyFragment : BaseFragment() {


    lateinit var privacyPolicyBinding: PrivacyPolicyBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        privacyPolicyBinding = PrivacyPolicyBinding.inflate(inflater, container, false)

        hideKeyBoard()
        setObservers()

        callPrivacyPolicy()



        val root: View = privacyPolicyBinding.root
        return root
    }

    private fun callPrivacyPolicy() {

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

        mUserViewModel.getAboutUs()
    }


    fun observePolicy(){

        mUserViewModel.getAboutUsLiveData().observe(viewLifecycleOwner, Observer {

            privacyPolicyBinding.privacyPolicyText.setText(it.policy)

        })


    }

    private fun setObservers(){

        observeExtras()
        observePolicy()

    }


    private fun observeExtras() {
        mUserViewModel!!.loading.observe(activity,
            { isLoading -> handleProgressLoader(isLoading!!) })
        mUserViewModel!!.dataLoadError.observe(activity, { s ->
            Log.e(TAG, "onChanged: $s")
            handleError(s)
        })
    }

}