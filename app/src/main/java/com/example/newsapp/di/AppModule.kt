package com.example.newsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.newsapp.NewsApplication
import com.example.newsapp.api.NewsApi
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.database.ArticlesDao
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.repository.RepoInterface
import com.example.newsapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): NewsApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit
            .Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java, "article_db.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(database: ArticleDatabase): ArticlesDao {
        return database.getArticleDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: ArticlesDao,api: NewsApi):RepoInterface{
        return NewsRepository(dao,api)
    }
    /*@Provides
    @Singleton
    fun provideApp():Application{
        return NewsApplication()
    }*/



}