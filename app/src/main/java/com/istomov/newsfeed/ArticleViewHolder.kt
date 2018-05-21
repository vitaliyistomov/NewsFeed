package com.istomov.newsfeed

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso

class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var icon: ImageView = itemView.findViewById(R.id.article_icon)
    var title: TextView = itemView.findViewById(R.id.article_title)
    var description: TextView = itemView.findViewById(R.id.article_description)

    fun invalidate(article: Article) {
        title.text = article.title
        description.text = article.description
        if (TextUtils.isEmpty(article.image)) {
            Picasso.get()
                    .load(R.drawable.ic_placeholder_48dp)
                    .into(icon)
        } else {
            Picasso.get()
                    .load(article.image)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.ic_placeholder_48dp)
                    .into(icon)
        }
    }
}