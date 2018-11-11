package garypan.com.imagebrowserforreddit.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.cardview_comment_listing.view.*
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.vo.RedditCommentResponse

class CommentAdapter(val comments : ArrayList<RedditCommentResponse.Children>, val context : Context) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RedditCommentResponse.Children>() {
            override fun areItemsTheSame(oldItem: RedditCommentResponse.Children, newItem: RedditCommentResponse.Children): Boolean =
                oldItem.data.id == newItem.data.id

            override fun areContentsTheSame(oldItem: RedditCommentResponse.Children, newItem: RedditCommentResponse.Children): Boolean =
                oldItem == newItem
        }
    }

    class CommentViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val comment : TextView = view.commentTextView
        val author : TextView = view.authorTextView
        val score : TextView = view.scoreTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_comment_listing, parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.comment.text = comments[position].data.body
        holder.author.text = comments[position].data.author
        holder.score.text = (comments[position].data.score).toString()
    }
}