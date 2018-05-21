package com.istomov.newsfeed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
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

        fab.setOnClickListener { showFeedsPicker() }

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        with(viewModel) {
            setSources(listOf("http://feeds.bbci.co.uk/news/uk/rss.xml"))
            getArticles().observe(this@MainActivity, Observer { onDataReceived(it) })
        }

        Snackbar.make(root, "The feed is sorted by the publication date, newest first", Snackbar.LENGTH_LONG).show()
    }

    private fun onDataRequested() {
        progressBar.visibility = View.VISIBLE
        list.visibility = View.GONE
    }

    private fun onDataReceived(items: List<Article>?) {
        progressBar.visibility = View.GONE
        list.visibility = View.VISIBLE
        adapter.setItems(items)
    }

    private fun showFeedsPicker() {
        AlertDialog.Builder(this)
                .setMultiChoiceItems(viewModel.urls, viewModel.checkedValues, { _, item, checked ->
                    viewModel.checkedValues[item] = checked
                })
                .setNegativeButton("Close", null)
                .setPositiveButton("OK", { _, _ ->
                    val selectedSources = mutableListOf<String>()
                    with(viewModel) {
                        urls.forEachIndexed({ i, value ->
                            if (checkedValues[i]) {
                                selectedSources.add(value)
                            }
                        })

                        val changed = setSources(selectedSources)
                        if (changed) {
                            onDataRequested()
                        }
                    }
                })
                .show()
    }
}
