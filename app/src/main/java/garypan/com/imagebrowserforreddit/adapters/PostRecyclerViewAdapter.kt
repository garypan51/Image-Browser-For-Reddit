package garypan.com.imagebrowserforreddit.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cardview_post_listing.view.*
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.models.Post
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import garypan.com.imagebrowserforreddit.helpers.RedditRequestManager


class PostRecyclerViewAdapter(val posts : ArrayList<Post>, val context : Context) :
    RecyclerView.Adapter<PostViewHolder>(){
    private val TAG = "PostsRecyclerView"
    private lateinit var circularProgressDrawable : CircularProgressDrawable
    val redditRequestManager = RedditRequestManager()

    fun addPosts(newPosts : ArrayList<Post>){
        newPosts.forEach{if((it !in posts)){posts.add(it)}}
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return PostViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_post_listing, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.postImage.layout(0,0,0,0)
        holder.title.text = posts[position].title.trim()
        val imageAspectRatio = ((posts[position].preview.images[0].source.width).toDouble()) / posts[position].preview.images[0].source.height
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        val height = when{
            posts[position].preview.images[0].source.height > screenHeight -> screenHeight
            else -> (screenWidth / imageAspectRatio).toInt()
        }
        holder.postImage.layoutParams.height = height

        val postUrl = fixUrl(posts[position].url)

        val options =
            RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)

        Glide.with(context)
            .load(postUrl)
            .apply(options)
            .into(holder.postImage)

        holder.commentButton.setOnClickListener{
            val comments = redditRequestManager.fetchComments(posts[position].subreddit, posts[position].id, context)
        }

    }

    private fun fixUrl(originalUrl : String) : String{
        return when{
            originalUrl.contains("imgur") && originalUrl.contains("gifv") && !originalUrl.contains("https")
            -> originalUrl.replace("http", "https").replace("gifv", "gif")

            originalUrl.contains("imgur") && originalUrl.contains("gifv")
            -> originalUrl.replace("gifv", "gif")

            originalUrl.contains("imgur") && !originalUrl.contains("https")
            -> originalUrl.replace("http", "https") + ".jpg"

            originalUrl.contains("imgur") -> originalUrl + ".jpg"

            originalUrl.contains("gfycat") && !originalUrl.contains("giant")
            -> originalUrl.substring(0, originalUrl.indexOf("//") + 2) + "giant." + originalUrl.substring(originalUrl.indexOf("//") + 2) + ".gif"
            else -> originalUrl
        }
    }
}

class PostViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val postImage = view.postImageView
    val title = view.titleTextView
    val commentButton = view.commentsButton

    fun getTextHeight() : Int {
        title.measure(0, 0)
        return title.measuredHeight
    }

    fun getButtonHeight() : Int {
        commentButton.measure(0, 0)
        return commentButton.measuredHeight
    }
}