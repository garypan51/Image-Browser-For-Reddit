package garypan.com.imagebrowserforreddit.views

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse


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
        window.animationStyle = R.style.Animation
        window.isFocusable = true
        window.showAtLocation(viewOfParentLayout, Gravity.NO_GRAVITY, 0, 0)
        val closeButton = imagePopUpView.findViewById<Button>(R.id.closeCommentsButton)
        closeButton.setOnClickListener{window.dismiss()}
        val imageView = imagePopUpView.findViewById<ImageView>(R.id.imageView)
        loadImage(context, imageView, postUrl)
        val titleTextView = imagePopUpView.findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = post.title
    }

    private fun loadImage(context : Context, imageView : ImageView, postUrl : String){
        val options =
            RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context)
            .load(postUrl)
            .apply(options)
            .into(imageView)
    }
}

