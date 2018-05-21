package com.istomov.newsfeed

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


class MainViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()

    private var sourceUrls = emptyList<String>()
    private var articles = MutableLiveData<List<Article>>()

    val urls = arrayOf(
            "http://feeds.bbci.co.uk/news/uk/rss.xml",
            "http://feeds.bbci.co.uk/news/technology/rss.xml",
            "http://feeds.reuters.com/reuters/UKdomesticNews?format=xml",
            "http://feeds.reuters.com/reuters/technologyNews?format=xml"
    )
    val checkedValues = booleanArrayOf(true, false, true, false)


    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun setSources(sourceUrls: List<String>): Boolean {
        if (this.sourceUrls != sourceUrls) {
            this.sourceUrls = sourceUrls
            loadArticles()
            return true
        }
        return false
    }

    fun getArticles(): MutableLiveData<List<Article>> {
        return articles
    }

    private fun loadArticles() {
        val disposable = Observable.fromIterable(sourceUrls)
                .flatMap { url ->
                    val results = BehaviorSubject.create<List<Article>>()
                    fetchFeed(url, results)
                    results.hide()
                }
                .buffer(sourceUrls.size)
                .first(emptyList())
                .flatMapObservable { Observable.fromIterable(it) }
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ list ->
                    list.sortByDescending { article -> article.pubDate }
                    articles.value = list
                })

        compositeDisposable.add(disposable)
    }

    // forgive me mom for using AsyncTasks-based API
    private fun fetchFeed(url: String, results: BehaviorSubject<List<Article>>) {
        //url of RSS feed
        val parser = Parser()
        parser.execute(url)
        parser.onFinish(object : Parser.OnTaskCompleted {

            override fun onTaskCompleted(list: ArrayList<Article>) {
                results.onNext(list)
            }

            override fun onError() {
            }
        })
    }
}
