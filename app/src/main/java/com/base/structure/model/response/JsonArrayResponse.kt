package com.base.structure.model.response

import com.google.gson.annotations.SerializedName

open class JsonArrayResponse<T> : BaseResponse() {


    @SerializedName("data")
     var list : List<T>? = null
}