package com.bangkit.hear4u.ui.article

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.hear4u.data.remote.response.ArticleData
import com.bangkit.hear4u.databinding.ActivityDetailArticleBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bumptech.glide.Glide

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private val viewModel by viewModels<DetailArtcleViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }


    private fun setupView() {
        val itemId = intent.getStringExtra("EXTRA_ID")
        if (itemId != null) {
            viewModel.getArticleById(itemId).observe(this) { result ->
                when (result) {
                    is StateResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is StateResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("Success Fetching Data", result.data.message)
                        result.data.data.let { setupBind(it) }
                    }

                    is StateResult.Error -> {
                        Log.e("Error Fetching Data", result.error )
                    }
                }
            }
        }
    }

    private fun setupBind(item: ArticleData){
        binding.tvTitle.text = item.title
        binding.tvContent.text = item.content
        Glide.with(binding.root.context)
            .load(item.thumbnail)
            .into(binding.ivDetailArticle)


    }
}