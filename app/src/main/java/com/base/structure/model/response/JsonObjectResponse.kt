package com.base.structure.model.response

import com.google.gson.annotations.SerializedName

open class JsonObjectResponse<T> : BaseResponse(){

    @SerializedName("data")
    var data : T? = null

}