package com.example.newsapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    lateinit var binding: FragmentBreakingNewsBinding
    private val viewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isScrolling = false
    var isLastPage = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel = (activity as NewsActivity).viewModel
     /*   val repository = NewsRepository(ArticleDatabase.invoke(requireContext()))
        val providerFactory = NewsViewModelProviderFactory(requireActivity().application,repository)
        viewModel = ViewModelProvider(this, providerFactory)[NewsViewModel::class.java]*/

        setUpRecycler()
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    isLoading = true
                }

                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    isLoading = false
                    Toast.makeText(requireContext(),"Error ${response.message}",Toast.LENGTH_LONG).show()
                }

                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    isLoading = false
                    newsAdapter.submitList(response.data?.articles)
                    val totalPages = (response.data?.totalResults?.div(Constants.QUERY_PAGE_SIZE))?.plus(2)
                    isLastPage = totalPages == viewModel.breakingNewsPage
                    if (isLastPage){
                        binding.rvBreakingNews.setPadding(0,0,0,0)
                    }
                }

            }
        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }


    }

    val myScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isLoading

            if (shouldPaginate) {
                viewModel.getBreakingNews(Constants.COUNTRY_CODE)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpRecycler() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addOnScrollListener(myScrollListener)
        }
    }
}
