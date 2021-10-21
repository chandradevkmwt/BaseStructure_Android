package com.base.structure.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.base.structure.base.BaseFragment
import com.base.structure.databinding.AboutUsBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AboutUsFragment : BaseFragment() {


    lateinit var aboutUsBinding: AboutUsBinding

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

        aboutUsBinding = AboutUsBinding.inflate(inflater, container, false)

        hideKeyBoard()
        setObservers()

        callAboutUs()



        val root: View = aboutUsBinding.root
        return root
    }

    private fun callAboutUs() {

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

        mUserViewModel.getAboutUs()

    }


    fun observeAboutUs(){

        mUserViewModel.getAboutUsLiveData().observe(viewLifecycleOwner, Observer {

            aboutUsBinding.aboutusText.setText(
                Html.fromHtml(
                it.about
            ))


        })


    }

    private fun setObservers(){

        observeExtras()
        observeAboutUs()

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