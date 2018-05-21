package com.istomov.newsfeed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.prof.rssparser.Article


class MainActivity : AppCompatActivity() {

    private lateinit var root: View
    private lateinit var progressBar: ProgressBar
    private lateinit var list: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: ArticleAdapter

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.root)
        progressBar = findViewById(R.id.loading_progress_bar)
        list = findViewById(R.id.list)
        fab = findViewById(R.id.fab)

        adapter = ArticleAdapter()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener({ Snackbar.make(root, "Blah", Snackbar.LENGTH_SHORT).show() })

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getArticles(listOf("http://feeds.bbci.co.uk/news/uk/rss.xml"))
                .observe(this, Observer { doOnDataArrives(it) })
    }

    private fun doOnDataArrives(items: List<Article>?) {
        progressBar.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter.setItems(items)
    }
}
