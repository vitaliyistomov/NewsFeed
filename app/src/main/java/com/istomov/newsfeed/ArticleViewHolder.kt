package com.istomov.newsfeed

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
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
        description.text = Html.fromHtml(article.description).toString()
        itemView.setOnClickListener {
            val intent = Intent(itemView.context, PreviewActivity::class.java)
            intent.putExtra(EXTRA_URL, article.link)
            itemView.context.startActivity(intent);
        }
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