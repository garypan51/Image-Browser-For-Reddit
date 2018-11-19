package garypan.com.imagebrowserforreddit.views

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.adapters.CommentAdapter
import garypan.com.imagebrowserforreddit.fragments.PostsFragment
import garypan.com.imagebrowserforreddit.utils.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditCommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentsPopUpView{
    private lateinit var window : PopupWindow
    private lateinit var commentsRecyclerView : RecyclerView

    fun createPopUpWindow(context : Context, postFrag : PostsFragment, viewOfParentLayout : View, subreddit : String, postId : String){
        if(::window.isInitialized){
            window.dismiss()
        }

        val commentsPopUpView = LayoutInflater.from(context).inflate(R.layout.popup_comments, null)
        window = PopupWindow(commentsPopUpView,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            viewOfParentLayout.measuredHeight / 3)
        window.contentView = commentsPopUpView
        window.animationStyle = R.style.Animation
        window.showAtLocation(viewOfParentLayout, Gravity.BOTTOM, 0, 0)

        val loadingSpinner = commentsPopUpView.findViewById<ProgressBar>(R.id.progressBar)
        setUpViews(commentsPopUpView, viewOfParentLayout, context)
        window.setOnDismissListener { postFrag.declarePopUpStatus(false) }
        fetchComments(context, loadingSpinner, subreddit, postId)
    }

    fun close(){
        window.dismiss()
    }

    private fun changeBackgroundColor(changeColorButton : Button, context : Context){
        val typedValue = TypedValue()
        val theme = context.theme

        when(changeColorButton.tag) {
            1 -> {
                theme?.resolveAttribute(R.attr.commentsBackgroundSolidColor, typedValue, true)
                changeColorButton.tag = 2
                changeColorButton.setBackgroundResource(R.drawable.ic_invert_colors_off)
            }


            2 -> {
                theme?.resolveAttribute(R.attr.commentsBackgroundTransparentColor, typedValue, true)
                changeColorButton.tag = 1
                changeColorButton.setBackgroundResource(R.drawable.ic_invert_colors)
            }
        }
        val backgroundColor = typedValue.data
        val popUpContainer = window.contentView.findViewById<ConstraintLayout>(R.id.commentPopUpContainer)
        popUpContainer.setBackgroundColor(backgroundColor)
    }

    private fun resizeCommentsPopUp(resizeButton : Button, viewOfParentLayout : View){
        when(resizeButton.tag){
            1 -> {
                window.update(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    viewOfParentLayout.measuredHeight)
                resizeButton.tag = 2
                resizeButton.setBackgroundResource(R.drawable.ic_chev_down)
            }

            2 -> {
                window.update(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    viewOfParentLayout.measuredHeight / 3)
                resizeButton.tag = 1
                resizeButton.setBackgroundResource(R.drawable.ic_chev_up)
            }
        }
    }

    private fun setUpViews(commentsPopUpView : View, viewOfParentLayout: View, context: Context){
        commentsRecyclerView = commentsPopUpView.findViewById(R.id.commentsRecyclerView)

        val closeButton = commentsPopUpView.findViewById<Button>(R.id.closeCommentsButton)
        closeButton.setOnClickListener{window.dismiss()}

        val resizeButton = commentsPopUpView.findViewById<Button>(R.id.expandCommentsButton)
        resizeButton.tag = 1
        resizeButton.setOnClickListener{ resizeCommentsPopUp(resizeButton, viewOfParentLayout) }

        val changeBackgroundColorButton = commentsPopUpView.findViewById<Button>(R.id.changeColorButton)
        changeBackgroundColorButton.tag = 1
        changeBackgroundColorButton.setOnClickListener { changeBackgroundColor(changeBackgroundColorButton, context) }
    }

    private fun fetchComments(context: Context, loadingSpinner: ProgressBar, subreddit: String, postId : String){
        val commentResponse = RedditApi.create().getComments(subreddit, postId, "top")
        commentResponse.enqueue(object : Callback<List<RedditCommentResponse.Result>> {
            override fun onFailure(call: Call<List<RedditCommentResponse.Result>>, t: Throwable) {
                call.clone().enqueue(this)
            }

            override fun onResponse(
                call: Call<List<RedditCommentResponse.Result>>,
                response: Response<List<RedditCommentResponse.Result>>
            ) {
                loadingSpinner.visibility = View.GONE
                val commentAdapter = CommentAdapter(response.body()?.get(1)?.data?.children!! , context)
                commentsRecyclerView.let{it.apply{
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = commentAdapter
                }}

                window.update()
            }
        })
    }

}