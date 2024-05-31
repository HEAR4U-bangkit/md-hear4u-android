package com.bangkit.hear4u.data.local.preferences

data class UserModel(
    val email: String,
    val fullname: String,
    val token: String,
    val isLogin: Boolean
)
