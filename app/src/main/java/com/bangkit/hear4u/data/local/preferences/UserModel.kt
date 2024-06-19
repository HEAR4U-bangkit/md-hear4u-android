package com.bangkit.hear4u.data.local.preferences

data class UserModel(
    val token: String,
    val id: String,
    val email: String,
    val fullname: String,
    val isLogin: Boolean = false
)
