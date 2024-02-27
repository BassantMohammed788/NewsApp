package com.example.newsapp.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName="articles")
data class Article(

   /* @PrimaryKey(autoGenerate = true)
    val author: String?,*/
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    @NonNull
    @PrimaryKey
    val url: String ,
    val urlToImage: String?
):Serializable