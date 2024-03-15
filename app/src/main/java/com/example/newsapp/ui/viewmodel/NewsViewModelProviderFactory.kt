package com.example.newsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.NewsApplication
import com.example.newsapp.repository.NewsRepository


/*
class NewsViewModelProviderFactory(val application: Application,private val repository: NewsRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NewsViewModel::class.java)){
            NewsViewModel(application,repository) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}*/
