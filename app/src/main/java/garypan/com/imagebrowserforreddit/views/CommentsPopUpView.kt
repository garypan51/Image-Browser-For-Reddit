package garypan.com.imagebrowserforreddit.views

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.ProgressBar
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
        setUpViews(commentsPopUpView, viewOfParentLayout)
        window.setOnDismissListener { postFrag.declarePopUpStatus(false) }
        fetchComments(context, loadingSpinner, subreddit, postId)
    }

    fun close(){
        window.dismiss()
    }

    private fun setUpViews(commentsPopUpView : View, viewOfParentLayout: View){
        val closeButton = commentsPopUpView.findViewById<Button>(R.id.closeCommentsButton)
        closeButton.setOnClickListener{window.dismiss()}
        commentsRecyclerView = commentsPopUpView.findViewById(R.id.commentsRecyclerView)
        val resizeButton = commentsPopUpView.findViewById<Button>(R.id.expandCommentsButton)
        resizeButton.tag = 1
        resizeButton.setOnClickListener{
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
                        650)
                    resizeButton.tag = 1
                    resizeButton.setBackgroundResource(R.drawable.ic_chev_up)
                }
            }
        }
    }

    private fun fetchComments(context: Context, loadingSpinner: ProgressBar, subreddit: String, postId : String){
        val commentResponse = RedditApi.create().getComments(subreddit, postId, "top")
        commentResponse.enqueue(object : Callback<List<RedditCommentResponse.Result>> {
            override fun onFailure(call: Call<List<RedditCommentResponse.Result>>, t: Throwable) {
                // retry
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