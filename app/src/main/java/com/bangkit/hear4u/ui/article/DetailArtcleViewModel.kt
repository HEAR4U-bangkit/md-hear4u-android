package com.bangkit.hear4u.ui.article

import androidx.lifecycle.ViewModel
import com.bangkit.hear4u.data.remote.repository.UserRepository


class DetailArtcleViewModel (private val repository: UserRepository) : ViewModel() {
    fun getArticleById(id : String) = repository.getArticleById(id)
}