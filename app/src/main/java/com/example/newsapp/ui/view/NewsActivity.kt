package com.example.newsapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.newsapp.R
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityNewsBinding
   // lateinit var viewModel: NewsViewModel
   // var repository: NewsRepository
    //private lateinit var providerFactory: NewsViewModelProviderFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView
        navController = Navigation.findNavController(this, R.id.newsNavHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }
}