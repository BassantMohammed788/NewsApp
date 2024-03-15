package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import retrofit2.Response

interface RepoInterface {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int)  :Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int) :Response<NewsResponse>

    suspend fun upsertArticle(article: Article) :Long
    suspend fun deleteArticle(article: Article) :Unit
    fun getSavedArticles() :LiveData<List<Article>>
}