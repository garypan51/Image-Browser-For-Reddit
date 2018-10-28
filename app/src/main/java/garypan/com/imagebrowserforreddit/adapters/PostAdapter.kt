package garypan.com.imagebrowserforreddit.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
//import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cardview_post_listing.view.*
import garypan.com.imagebrowserforreddit.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse

class PostAdapter(val context : Context) :
    PagedListAdapter<RedditPostResponse.Children, PostViewHolder>(diffCallback){
    private val TAG = "PostsRecyclerView"
    private lateinit var circularProgressDrawable : CircularProgressDrawable

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RedditPostResponse.Children>() {
            override fun areItemsTheSame(oldItem: RedditPostResponse.Children, newItem: RedditPostResponse.Children): Boolean =
                oldItem.data.id == newItem.data.id

            override fun areContentsTheSame(oldItem: RedditPostResponse.Children, newItem: RedditPostResponse.Children): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return PostViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_post_listing, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if (!(getItem(position)!!.data.containsImages())){
            holder.entireCard.visibility = View.INVISIBLE
            holder.entireCard.layoutParams.height = 0
            return
        }

        holder.postImage.layout(0,0,0,0)
        holder.title.text = getItem(position)!!.data.title.trim()

        val imageAspectRatio = ((getItem(position)!!.data.preview.images[0].source.width).toDouble()) / getItem(position)!!.data.preview.images[0].source.height
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        val height = if ((screenWidth / imageAspectRatio).toInt() > screenHeight) {
                screenHeight - 400
            }
            else {(screenWidth / imageAspectRatio).toInt()}
        holder.postImage.layoutParams.height = height

        val postUrl = adjustUrl(getItem(position)!!.data.url)

        val options =
            RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)

        Glide.with(context)
            .load(postUrl)
            .apply(options)
            .into(holder.postImage)
    }

    private fun adjustUrl(originalUrl : String) : String{
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
    val entireCard = view.cardView
    val postImage = view.postImageView
    val title = view.titleTextView
    val commentButton = view.commentsButton
}