package com.base.structure.model.response;

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("counts")
    @Expose
    var counts: Int = 0

   // var isIdeal : Boolean =false

}
