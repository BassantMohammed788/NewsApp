package com.example.newsapp.repository

import com.example.newsapp.api.NewsApi
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.database.ArticlesDao
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val dao: ArticlesDao, private val api: NewsApi):RepoInterface {
    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    override suspend fun upsertArticle(article: Article) = dao.upsert(article)
    override suspend fun deleteArticle(article: Article) = dao.deleteArticle(article)
    override fun getSavedArticles()=dao.getAllArticles()

}