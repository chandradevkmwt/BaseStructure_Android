package com.base.structure.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.structure.utils.StaticData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class BaseViewModel : ViewModel() {

    var dataLoadError : MutableLiveData<String> = MutableLiveData()
    var loading : MutableLiveData<Boolean> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable();
    var extraData : MutableLiveData<HashMap<String, Any?>> = MutableLiveData()

    fun handleError(error: Throwable){
        if (error is HttpException) {
            val jsonErrorResponse = JSONObject(error.response()?.errorBody()?.string())
            dataLoadError.value = jsonErrorResponse?.optString("message")
        } else if (error is SocketTimeoutException) {
            dataLoadError.value = StaticData.TIMEOUT
        } else if (error is IOException) {
            dataLoadError.value = StaticData.UNDEFINED
        } else {
            dataLoadError.value = StaticData.UNDEFINED
        }
    }

    protected fun addToDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
        compositeDisposable.add(disposable)
    }

    fun onStop() {
        compositeDisposable.clear()
    }

}
