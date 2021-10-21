package com.base.structure.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.structure.base.BaseFragment
import com.base.structure.databinding.EditProfileBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.mPrefs
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class EditProfileFragment : BaseFragment() {

    lateinit var editProfileBinding: EditProfileBinding

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

        editProfileBinding = EditProfileBinding.inflate(inflater, container, false)

        hideKeyBoard()
        setObservers()

        callEditProfiledata(mPrefs.prefUserDetails?.id.toString())

        editProfileBinding.editProfile.setOnClickListener(View.OnClickListener {
            if (isValid()){

                callEditProfile(mPrefs.prefUserDetails?.id.toString(),editProfileBinding.editFullName.text.toString(),editProfileBinding.editEmail.text.toString(),
                    editProfileBinding.editPlotNo.text.toString(),editProfileBinding.editStreet.text.toString(),editProfileBinding.editLandmark.text.toString(),
                    editProfileBinding.editCity.text.toString(),editProfileBinding.editState.text.toString(),editProfileBinding.editPincode.text.toString())

            }
        })

        val root: View = editProfileBinding.root
        return root
    }

    private fun callEditProfiledata(userid: String) {

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

//        mUserViewModel.editProfileCallData(userid)

    }

    private fun callEditProfile(
        userid: String,
        name: String,
        email: String,
        plotno: String,
        street: String,
        landmark: String,
        city: String,
        state: String,
        pincode: String
    ) {

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

//        mUserViewModel.editProfileData(userid,name,email,plotno,street,landmark,city,state,pincode)

    }


    fun observeEditProfile(){

//        mUserViewModel.getEditProfileLiveData().observe(viewLifecycleOwner, Observer {
//
//
//            editProfileBinding.editCity.setText(it.city)
//            editProfileBinding.editEmail.setText(it.email)
//            editProfileBinding.editFullName.setText(it.name)
//            editProfileBinding.editLandmark.setText(it.landmark)
//            editProfileBinding.editPincode.setText(it.pincode)
//            editProfileBinding.editPlotNo.setText(it.plotno)
//            editProfileBinding.editState.setText(it.state)
//            editProfileBinding.editStreet.setText(it.streetname)
//
//
//        })


    }

    private fun setObservers(){

        observeExtras()
        observeEditProfile()

    }


    private fun observeExtras() {
        mUserViewModel!!.loading.observe(activity,
            { isLoading -> handleProgressLoader(isLoading!!) })
        mUserViewModel!!.dataLoadError.observe(activity, { s ->
            Log.e(TAG, "onChanged: $s")
            handleError(s)
        })
    }

    private fun isValid(): Boolean {

        if (TextUtils.isEmpty(editProfileBinding.editFullName.text.toString().trim())) {
            editProfileBinding.editFullName.error = "please enter name"
            editProfileBinding.editFullName.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(editProfileBinding.editEmail.text.toString().trim())) {
            editProfileBinding.editEmail.error = "please enter email"
            editProfileBinding.editEmail.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editPlotNo.text.toString().trim())) {
            editProfileBinding.editPlotNo.error = "please enter plotno"
            editProfileBinding.editPlotNo.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editStreet.text.toString().trim())) {
            editProfileBinding.editStreet.error = "please enter street/colony"
            editProfileBinding.editStreet.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editLandmark.text.toString().trim())) {
            editProfileBinding.editLandmark.error = "please enter landmark"
            editProfileBinding.editLandmark.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editCity.text.toString().trim())) {
            editProfileBinding.editCity.error = "please enter city"
            editProfileBinding.editCity.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editState.text.toString().trim())) {
            editProfileBinding.editState.error = "please enter state"
            editProfileBinding.editState.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(editProfileBinding.editPincode.text.toString().trim())) {
            editProfileBinding.editPincode.error = "please enter pincode"
            editProfileBinding.editPincode.requestFocus()
            return false
        }

        return true
    }

}