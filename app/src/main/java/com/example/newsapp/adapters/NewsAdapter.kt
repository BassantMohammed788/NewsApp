package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.models.Article

class MyDiffUtil : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
class NewsAdapter: ListAdapter<Article, NewsAdapter.MyViewHolder>(MyDiffUtil()) {
        lateinit var context: Context

        inner class MyViewHolder(val binding: ItemArticlePreviewBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater: LayoutInflater =
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            context = parent.context
            val binding = ItemArticlePreviewBinding.inflate(inflater, parent, false)
            return MyViewHolder(binding)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val currentObject = getItem(position)
            holder.binding.tvTitle.text = currentObject.title
            holder.binding.tvSource.text = currentObject.source?.name
            holder.binding.tvDescription.text = currentObject.description
            holder.binding.tvPublishedAt.text = currentObject.publishedAt

            Glide.with(context)
                .load(currentObject.urlToImage)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.binding.ivArticleImage)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(currentObject)  }
            }


        }

    private var onItemClickListener :((Article) -> Unit)? = null

    fun setOnItemClickListener(listener:(Article) -> Unit){
        onItemClickListener = listener
    }
    }