package com.example.newsapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.Article
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding
    private val viewModel: NewsViewModel by viewModels()
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
    /*    val repository = NewsRepository(ArticleDatabase.invoke(requireContext()))
        val providerFactory = NewsViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)
*/
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        viewModel.getSavedArticles().observe(viewLifecycleOwner) { articles ->
            savedArticles = articles
            if (savedArticles.contains(article)) {
                binding.fab.setImageResource(R.drawable.fav_icon)
            }else{
                binding.fab.setImageResource(R.drawable.base_favorite_icon)
            }
        }

        binding.fab.setOnClickListener {
            if (!savedArticles.contains(article)) {
                viewModel.saveArticle(article)
                Snackbar.make(it, "Article Added Successfully", Snackbar.LENGTH_SHORT).show()
                binding.fab.setImageResource(R.drawable.fav_icon)
            } else {
                binding.fab.setImageResource(R.drawable.base_favorite_icon)
                viewModel.deleteArticle(article)
                Snackbar.make(it, "Article deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles().observe(viewLifecycleOwner) { articles ->
            savedArticles = articles
            if (savedArticles.contains(args.article)) {
                binding.fab.setImageResource(R.drawable.fav_icon)
            }else{
                binding.fab.setImageResource(R.drawable.base_favorite_icon)
            }
        }
    }
}