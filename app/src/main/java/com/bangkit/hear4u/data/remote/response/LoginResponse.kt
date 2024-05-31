package com.bangkit.hear4u.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: LoginData, // Mengubah referensi Data menjadi LoginData

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class LoginUser( // Mengganti nama dari User ke LoginUser

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("email")
	val email: String
)

data class LoginData(

	@field:SerializedName("user")
	val user: LoginUser, // Menyesuaikan dengan nama baru

	@field:SerializedName("token")
	val token: String
)