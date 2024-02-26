package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding
    lateinit var viewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()
    var savedArticles = listOf<Article>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = NewsRepository(ArticleDatabase.invoke(requireContext()))
        val providerFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)

        val article = args.article
        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        viewModel.getSavedArticles().observe(viewLifecycleOwner){articles->
           savedArticles=articles
        }
        if (savedArticles.contains(article)){
            binding.fab.setImageResource(R.drawable.fav_icon)
        }
        binding.fab.setOnClickListener {
            if (!savedArticles.contains(article)){
                viewModel.saveArticle(article)
                Snackbar.make(it,"Article Added Successfully",Snackbar.LENGTH_SHORT).show()
                binding.fab.setImageResource(R.drawable.fav_icon)
            }else{
                viewModel.deleteArticle(article)
                Snackbar.make(it,"Article deleted Successfully",Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
                binding.fab.setImageResource(R.drawable.base_favorite_icon)
            }

        }
    }

}