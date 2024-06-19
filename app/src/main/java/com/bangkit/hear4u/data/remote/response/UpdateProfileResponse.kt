package com.bangkit.hear4u.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

    @field:SerializedName("data")
    val data: profileData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,
)

data class profileData(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,
)
