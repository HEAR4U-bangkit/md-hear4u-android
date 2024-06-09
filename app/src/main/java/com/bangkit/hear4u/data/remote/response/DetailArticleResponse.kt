package com.bangkit.hear4u.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

	@field:SerializedName("data")
	val data: ArticleData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class ArticleData(

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)
