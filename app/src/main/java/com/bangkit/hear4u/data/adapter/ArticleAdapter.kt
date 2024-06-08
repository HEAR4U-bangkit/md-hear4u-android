package com.bangkit.hear4u.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.hear4u.data.remote.response.DataItem
import com.bangkit.hear4u.databinding.ItemBinding
import com.bumptech.glide.Glide

class  ArticleAdapter : ListAdapter<DataItem, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    class ArticleViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            binding.tvItemName.text = item.title
            binding.tvItemDescription.text = item.content // Assuming 'email' is a placeholder for the article description
            Glide.with(itemView.context)
                .load(item.thumbnail) // Assuming 'photoUrl' is a field in ListStoryItem
                .into(binding.imgItemPhoto)
            itemView.setOnClickListener {

            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}