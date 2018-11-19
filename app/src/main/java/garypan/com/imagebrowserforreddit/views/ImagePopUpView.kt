package garypan.com.imagebrowserforreddit.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import android.text.method.ScrollingMovementMethod




object ImagePopUpView{
    private lateinit var window : PopupWindow

    fun createPopUpWindow(context : Context, viewOfParentLayout : View, postUrl : String, post : RedditPostResponse.Post){
        if(::window.isInitialized){
            window.dismiss()
        }

        val imagePopUpView = LayoutInflater.from(context).inflate(R.layout.popup_image, null)

        window = PopupWindow(imagePopUpView,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT)

        window.contentView = imagePopUpView

        // Uncomment to add animation to pop up window
//        window.animationStyle = R.style.Animation
        window.isFocusable = true

        window.showAtLocation(viewOfParentLayout, Gravity.NO_GRAVITY, 0, 0)
        setUpViews(context, imagePopUpView, postUrl, post)
    }

    private fun setUpViews(context: Context, imagePopUpView : View, postUrl: String, post: RedditPostResponse.Post){
        val closeButton = imagePopUpView.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener{window.dismiss()}
        val imageView = imagePopUpView.findViewById<ImageView>(R.id.imageView)
        loadImage(context, imageView, postUrl)
        val titleTextView = imagePopUpView.findViewById<TextView>(R.id.titleTextView)
        titleTextView.movementMethod = ScrollingMovementMethod()
        titleTextView.text = post.title

        val posterTextView = imagePopUpView.findViewById<TextView>(R.id.posterTextView)
        posterTextView.text = context.getString(R.string.poster_name, post.author)
        val scoreTextView = imagePopUpView.findViewById<TextView>(R.id.scoreTextView)
        scoreTextView.text = context.getString(R.string.score, post.score)
        val silverTextView = imagePopUpView.findViewById<TextView>(R.id.silverAwardTextView)
        val goldTextView = imagePopUpView.findViewById<TextView>(R.id.goldAwardTextView)
        val platTextView = imagePopUpView.findViewById<TextView>(R.id.platAwardTextView)

        val gilds = post.gildings
        if(gilds.anyGilds()) {
            if(gilds.awardedSilver()) {
                silverTextView.visibility = View.VISIBLE
                silverTextView.text = gilds.gid_1.toString()
            }
            if(gilds.awardedGold()) {
                goldTextView.visibility = View.VISIBLE
                goldTextView.text = gilds.gid_2.toString()
            }
            if(gilds.awardedPlat()) {
                platTextView.visibility = View.VISIBLE
                platTextView.text = gilds.gid_3.toString()
            }
        }
    }

    private fun loadImage(context : Context, imageView : ImageView, postUrl : String){
        val progressBar = window.contentView.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val options =
            RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context)
            .load(postUrl)
            .apply(options)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean) : Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                             dataSource: DataSource?, isFirstResource: Boolean) : Boolean {
                    progressBar.visibility = View.INVISIBLE
                    return false
                }
            })
            .into(imageView)
    }
}

