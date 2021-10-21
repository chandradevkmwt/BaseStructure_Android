package com.base.structure.apiservices

import com.base.structure.model.response.*
import com.google.protobuf.Any
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("signup")
    fun signup(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Single<JsonObjectResponse<UserDetailsResponse>>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Single<JsonObjectResponse<UserDetailsResponse>>


    @FormUrlEncoded
    @POST("otpvarify")
    fun verifyOtp(
        @Field("mobile") mobile: String,
        @Field("otp") otp: Int
    ): Single<JsonObjectResponse<OtpResponse>>

    @FormUrlEncoded
    @POST("resendotp")
    fun resendOtp(
        @Field("mobile") mobile: String
    ): Single<JsonObjectResponse<ResendOtpResponse>>


    @FormUrlEncoded
    @POST("forgetpassword")
    fun forgotPassword(
        @Field("mobile") mobile: String
    ): Single<JsonObjectResponse<Any>>


    @FormUrlEncoded
    @POST("changepassword")
    fun changePassword(
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("newpassword") newpassword: String
    ): Single<JsonObjectResponse<Any>>

//    @FormUrlEncoded
//    @POST("editprofile")
//    fun editProfile(
//        @Field("userid") userid: String,
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("plotno") plotno: String,
//        @Field("streetname") street: String,
//        @Field("landmark") landmark: String,
//        @Field("city") city: String,
//        @Field("state") state: String,
//        @Field("pincode") pincode: String
//    ): Single<JsonObjectResponse<EditProfileResponse>>
//
//    @FormUrlEncoded
//    @POST("editprofile")
//    fun editProfileCall(
//        @Field("userid") userid: String
//    ): Single<JsonObjectResponse<EditProfileResponse>>

    @GET("aboutus")
    fun aboutUs(): Single<JsonObjectResponse<AboutUsResponse>>


//    @Multipart
//    @POST("setprofileimage")
//    fun editProfileImage(
//        @Part("userid") userid: RequestBody,
//        @Part image: MultipartBody.Part
//    ): Single<JsonObjectResponse<EditProfileImageResponse>>

}