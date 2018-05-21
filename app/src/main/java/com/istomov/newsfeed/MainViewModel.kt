package com.istomov.newsfeed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.prof.rssparser.Article
import com.prof.rssparser.Parser


class MainViewModel : ViewModel() {
    private var articles: MutableLiveData<List<Article>> = MutableLiveData()
    private var dataLoaded = false

    fun getArticles(sourceUrls: List<String>): LiveData<List<Article>> {
        if (!dataLoaded) {
            loadArticles(sourceUrls)
            dataLoaded = true
        }
        return articles
    }

    private fun loadArticles(sourceUrls: List<String>) {
        fetchFeed(sourceUrls[0])
    }

    // forgive me mom for using AsyncTasks
    private fun fetchFeed(url: String) {
        //url of RSS feed
        val parser = Parser()
        parser.execute(url)
        parser.onFinish(object : Parser.OnTaskCompleted {

            override fun onTaskCompleted(list: ArrayList<Article>) {
                articles.value = list
            }

            override fun onError() {
            }
        })
    }
}
