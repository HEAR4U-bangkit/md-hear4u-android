package com.bangkit.hear4u.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(

	@field:SerializedName("data")
	val data: passwordData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class passwordData(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
