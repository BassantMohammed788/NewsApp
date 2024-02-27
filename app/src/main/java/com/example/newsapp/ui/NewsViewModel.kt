package com.example.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApplication
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(application: Application, val repository: NewsRepository) :
    AndroidViewModel(application) {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews(Constants.COUNTRY_CODE)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = responseResult
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = responseResult.articles
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
                if (searchNewsResponse == null) {
                    searchNewsResponse = responseResult
                } else {
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



      fun getBreakingNews(countryCode: String) = viewModelScope.launch{
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {

                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error(exception.message!!))
            }

        }

    }

     fun searchNews(query: String) =viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchNews(query, searchNewsPage)
                searchNews.postValue(handleBreakingNewsResponse(response))
            } else {

                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error(exception.message!!))
            }

        }
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


    private fun hasInternetConnection(): Boolean {
        val application = getApplication<NewsApplication>()
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(TRANSPORT_WIFI) ||
                capabilities.hasTransport(TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(TRANSPORT_ETHERNET)


    }

}