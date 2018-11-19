package garypan.com.imagebrowserforreddit.adapters

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cardview_post_listing.view.*
import garypan.com.imagebrowserforreddit.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.card.MaterialCardView
import garypan.com.imagebrowserforreddit.paging.Status
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse

class PostAdapter(val context : Context, private val postButtonsClickListener : OnPostButtonsClickListener) :
    PagedListAdapter<RedditPostResponse.Children, PostAdapter.PostViewHolder>(diffCallback){
    private lateinit var circularProgressDrawable : CircularProgressDrawable
    private var currentLayout = "Linear"
    private var status = Status.RUNNING

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RedditPostResponse.Children>() {
            override fun areItemsTheSame(oldItem: RedditPostResponse.Children, newItem: RedditPostResponse.Children): Boolean =
                oldItem.data.id == newItem.data.id

            override fun areContentsTheSame(oldItem: RedditPostResponse.Children, newItem: RedditPostResponse.Children): Boolean =
                oldItem == newItem
        }
    }

    class PostViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val postCardView : MaterialCardView = view.cardView
        val postImage : ImageView = view.postImageView
        val title : TextView = view.titleTextView
        val viewPictureButton : Button = view.viewPictureButton
        val commentsButton : Button = view.commentsButton
        val browserButton : Button = view.openInBrowserButton
        val scoreTextView : TextView = view.scoreTextView
        val silverTextView : TextView = view.silverTextView
        val goldTextView : TextView = view.goldTextView
        val platTextView : TextView = view.platTextView
    }

    interface OnPostButtonsClickListener{
        fun openCommentsView(postId : String)
        fun openImageView(postUrl : String, post : RedditPostResponse.Post)
//        fun changeCommentsBackground()
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
            holder.postCardView.visibility = View.GONE
            holder.postCardView.layoutParams.height = 0
            return
        }

        val post = getItem(position)?.data
        holder.postImage.layout(0,0,0,0)
        holder.title.text = post!!.title.trim()
        setImageViewDimensions(post, holder)
        val postUrl = (getItem(position)!!.data.preview.images[0].source.url).replace("amp;", "")
        setPostButtonListeners(post, holder, position, postUrl)
        setUpViews(post, holder)

        val options = RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable)


        Glide.with(context)
            .load(postUrl)
            .apply(options)
            .into(holder.postImage)
    }

    private fun setImageViewDimensions(post : RedditPostResponse.Post, holder: PostViewHolder){
        val imageAspectRatio = ((post.preview.images[0].source.width).toDouble()) / post.preview.images[0].source.height
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels

        val imageViewWidth = if(currentLayout == context.resources.getString(R.string.layoutPrefLinearValue)) { screenWidth }
        else{ screenWidth / 2}


        holder.postImage.layoutParams.width = imageViewWidth

        holder.postImage.layoutParams.height = if ((imageViewWidth.toDouble() / imageAspectRatio).toInt() > screenHeight) {
            screenHeight - 400
        } else {
            (imageViewWidth.toDouble() / imageAspectRatio).toInt()
        }
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

    private fun setPostButtonListeners(post : RedditPostResponse.Post, holder: PostViewHolder, position: Int, postUrl : String){
        holder.postImage.setOnClickListener{postButtonsClickListener.openImageView(postUrl, post)}
        holder.viewPictureButton.setOnClickListener{postButtonsClickListener.openImageView(postUrl, post)}
        holder.commentsButton.setOnClickListener{postButtonsClickListener.openCommentsView(getItem(position)!!.data.id)}

        holder.browserButton.setOnClickListener{
            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(context.resources.getString(R.string.base_url) + post.permalink ))
            val activities: List<ResolveInfo> = context.packageManager.queryIntentActivities(webIntent, 0)
            val isIntentSafe: Boolean = activities.isNotEmpty()
            if (isIntentSafe) {
                context.startActivity(webIntent)
            }
        }
    }

    private fun setUpViews(post : RedditPostResponse.Post, holder: PostViewHolder){
        holder.scoreTextView.text = context.getString(R.string.points, post.score)
        val gilds = post.gildings
        if(gilds.anyGilds()) {
            holder.silverTextView.visibility = if(gilds.awardedSilver()) View.VISIBLE else View.GONE
            holder.silverTextView.text = gilds.gid_1.toString()

            holder.goldTextView.visibility = if(gilds.awardedGold()) View.VISIBLE else View.GONE
            holder.goldTextView.text = gilds.gid_2.toString()

            holder.platTextView.visibility = if(gilds.awardedPlat()) View.VISIBLE else View.GONE
            holder.platTextView.text = gilds.gid_3.toString()
        }
    }

    fun setStatus(status: Status) {
        this.status = status
        notifyItemChanged(super.getItemCount())
    }

    fun setLayout(layout : String){
        currentLayout = layout
    }

}