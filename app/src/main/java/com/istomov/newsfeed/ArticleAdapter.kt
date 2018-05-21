package com.istomov.newsfeed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.prof.rssparser.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleViewHolder>() {

    private val items = ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(root)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.invalidate(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<Article>?) {
        if (newItems != null) {
            items.clear();
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }
}