package com.example.newsapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var breakingNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews(Constants.COUNTRY_CODE)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))

    }

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(query, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse=responseResult
                }else{
                    var oldArticles = breakingNewsResponse?.articles
                    var newArticles = responseResult.articles
                    oldArticles?.addAll(newArticles)
                }

                Log.e("ViewModel", "success: ${Gson().toJson(response.body())}")
                return Resource.Success(breakingNewsResponse ?: responseResult)
            }
        }
        Log.e("ViewModel", "Fail: $response")
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                searchNewsPage++
                if (searchNewsResponse == null){
                    searchNewsResponse=responseResult
                }else{
                    var oldArticles = searchNewsResponse?.articles
                    var newArticles = responseResult.articles
                    oldArticles?.addAll(newArticles)
                }

                Log.e("ViewModel", "SearchSuccess: ${Gson().toJson(response.body())}")
                return Resource.Success(searchNewsResponse ?: responseResult)
            }
        }
        Log.e("ViewModel", "searchFail: $response")
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            repository.upsertArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }

    fun getSavedArticles() = repository.getSavedArticles()


}