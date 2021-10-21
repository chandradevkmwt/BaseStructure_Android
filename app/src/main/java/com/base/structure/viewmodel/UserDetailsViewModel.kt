package com.base.structure.viewmodel

import androidx.lifecycle.MutableLiveData
import com.base.structure.apiservices.ApiInterface
import com.base.structure.base.BaseViewModel
import com.base.structure.model.response.*
import com.base.structure.utils.StaticData
import com.google.protobuf.Any
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(val appApiService: ApiInterface) : BaseViewModel() {

//    private val userDetailRepo : UserDetailsRepository = UserDetailsRepository(appApiService)
  //  private val registerLivedata = MutableLiveData<UserDetailsModel>()
    private val loginLiveData = MutableLiveData<UserDetailsResponse>()

    private val signUpLiveData = MutableLiveData<UserDetailsResponse>()

    private val forgotPasswordLiveData = MutableLiveData<Any>()
    private val navHeaderLiveData = MutableLiveData<UserDetailsResponse>()


    private val changePasswordLiveData = MutableLiveData<Boolean>()
//    private val editProfileLiveData = MutableLiveData<EditProfileResponse>()
//    private val editProfileImageLiveData = MutableLiveData<EditProfileImageResponse>()
    private val aboutUsLiveData = MutableLiveData<AboutUsResponse>()
    private val sellsPostLiveData = MutableLiveData<Any>()
    private val sendSmsLiveData = MutableLiveData<Any>()
    private val notificationStatusLiveData = MutableLiveData<Boolean>()



    fun userLogin(mobile: String,password: String){
        loading.value = true
      //  dataLoadError.value = ""

       appApiService.login(mobile,password)
           .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result ->

                if(result != null){
                    if (result.status){
                        if (result.data==null)
                            dataLoadError.value = result!!.message
                        else
                            loginLiveData.value=result!!.data
                    }else{
                        dataLoadError.value = result!!.message
                    }

                }else{
                    dataLoadError.value = StaticData.UNDEFINED
                }
                loading.value = false
            }, { error ->
                handleError(error)
                loading.value = false
            })
    }

    fun signUp(mobile: String,password: String){
        loading.value = true
        //  dataLoadError.value = ""

        appApiService.signup(mobile,password)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result ->

                if(result != null){
                    if (result.status){
                        if (result.data==null)
                            dataLoadError.value = result!!.message
                        else
                            signUpLiveData.value=result!!.data
                    }else{
                        dataLoadError.value = result!!.message
                    }

                }else{
                    dataLoadError.value = StaticData.UNDEFINED
                }
                loading.value = false
            }, { error ->
                handleError(error)
                loading.value = false
            })
    }


    fun forgotPassword(mobile: String){
        loading.value = true
        //  dataLoadError.value = ""

        appApiService.forgotPassword(mobile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result ->

                if(result != null){
                    if (result.status){
                        if (result.data==null)
                            dataLoadError.value = result!!.message
                        else
                            forgotPasswordLiveData.value = result!!.data
                    }else{
                        dataLoadError.value = result!!.message
                    }

                }else{
                    dataLoadError.value = StaticData.UNDEFINED
                }
                loading.value = false
            }, { error ->
                handleError(error)
                loading.value = false
            })
    }









    fun getChangePassword(mobile: String,password: String,newPassword:String){
        loading.value = true
        //  dataLoadError.value = ""

        appApiService.changePassword(mobile,password,newPassword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result ->

                if(result != null){
                    if (result.status){
                            changePasswordLiveData.value = result.status
                    }else{
                        dataLoadError.value = result!!.message
                    }

                }else{
                    dataLoadError.value = StaticData.UNDEFINED
                }
                loading.value = false
            }, { error ->
                handleError(error)
                loading.value = false
            })
    }

//    fun editProfileData(userid: String,
//                        name: String,
//                        email: String,
//                        plotno: String,
//                        street: String,
//                        landmark: String,
//                        city: String,
//                        state: String,
//                        pincode: String){
//        loading.value = true
//        //  dataLoadError.value = ""
//
//        appApiService.editProfile(userid,name,email,plotno,street,landmark,city,state,pincode)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({result ->
//
//                if(result != null){
//                    if (result.status){
//                        if (result.data==null)
//                            dataLoadError.value = result!!.message
//                        else
//                            editProfileLiveData.value = result!!.data
//                    }else{
//                        dataLoadError.value = result!!.message
//                    }
//
//                }else{
//                    dataLoadError.value = StaticData.UNDEFINED
//                }
//                loading.value = false
//            }, { error ->
//                handleError(error)
//                loading.value = false
//            })
//    }
//
//    fun editProfileCallData(userid: String){
//        loading.value = true
//        //  dataLoadError.value = ""
//
//        appApiService.editProfileCall(userid)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({result ->
//
//                if(result != null){
//                    if (result.status){
//                        if (result.data==null)
//                            dataLoadError.value = result!!.message
//                        else
//                            editProfileLiveData.value = result!!.data
//                    }else{
//                        dataLoadError.value = result!!.message
//                    }
//
//                }else{
//                    dataLoadError.value = StaticData.UNDEFINED
//                }
//                loading.value = false
//            }, { error ->
//                handleError(error)
//                loading.value = false
//            })
//    }
//
//    fun editProfileImageCall(userid: RequestBody,image: MultipartBody.Part){
//        loading.value = true
//        //  dataLoadError.value = ""
//
//        appApiService.editProfileImage(userid,image)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({ result ->
//
//                if(result != null){
//                    if (result.status){
//                        if (result.data==null)
//                            dataLoadError.value = result!!.message
//                        else
//                            editProfileImageLiveData.value= result!!.data
//                    }else{
//                        dataLoadError.value = result!!.message
//                    }
//
//                }else{
//                    dataLoadError.value = StaticData.UNDEFINED
//                }
//                loading.value = false
//            }, { error ->
//                handleError(error)
//                loading.value = false
//            })
//    }

    fun getAboutUs(){
        loading.value = true
        //  dataLoadError.value = ""

        appApiService.aboutUs()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result ->

                if(result != null){
                    if (result.status){

                        if (result.data==null)
                            dataLoadError.value = result!!.message
                        else
                            aboutUsLiveData.value = result!!.data

                    }else{
                        dataLoadError.value = result!!.message
                    }

                }else{
                    dataLoadError.value = StaticData.UNDEFINED
                }
                loading.value = false
            }, { error ->
                handleError(error)
                loading.value = false
            })
    }



    fun getLoginLiveData() = loginLiveData
    fun getSignUpLiveData() = signUpLiveData
    fun getForgotPasswordLiveData() = forgotPasswordLiveData

    fun getNavDrawerLiveData() = navHeaderLiveData
    fun getChangePasswordLiveData() = changePasswordLiveData
//    fun getEditProfileLiveData() = editProfileLiveData
    fun getAboutUsLiveData() = aboutUsLiveData
    fun getSellsProductLiveData() = sellsPostLiveData
//    fun getEditProfileImageLiveData() = editProfileImageLiveData
    fun getSendSmsLiveData() = sendSmsLiveData
    fun getNotificationStatusLiveData() = notificationStatusLiveData



}

