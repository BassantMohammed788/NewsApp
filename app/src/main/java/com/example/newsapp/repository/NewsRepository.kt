package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val database: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsertArticle(article: Article) = database.getArticleDao().upsert(article)
    suspend fun deleteArticle(article: Article) = database.getArticleDao().deleteArticle(article)
    fun getSavedArticles()=database.getArticleDao().getAllArticles()

}