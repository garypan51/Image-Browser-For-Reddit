package garypan.com.imagebrowserforreddit.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import garypan.com.imagebrowserforreddit.R
import com.google.android.material.snackbar.Snackbar
import garypan.com.imagebrowserforreddit.adapters.PostAdapter
import garypan.com.imagebrowserforreddit.interfaces.OnRecyclerViewChangedListener
import garypan.com.imagebrowserforreddit.paging.PostViewModel
import garypan.com.imagebrowserforreddit.paging.Status
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper
import garypan.com.imagebrowserforreddit.views.CommentsPopUpView
import garypan.com.imagebrowserforreddit.views.ImagePopUpView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_posts.*
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper.get
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper.set
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse

@SuppressLint("ClickableViewAccessibility")
class PostsFragment : Fragment(), PostAdapter.OnPostButtonsClickListener, OnRecyclerViewChangedListener {
    private lateinit var subreddit : String
    private lateinit var viewOfLayout: View
    private lateinit var viewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var prefs : SharedPreferences
    private lateinit var currentLayout : String
    private var commentsPopUpOpened = false
    var disposable: Disposable? = null

    companion object {
        private const val SUBREDDIT_KEY = "subreddit"
        fun newInstance(subreddit: String): PostsFragment {
            val args = Bundle()
            args.putSerializable(SUBREDDIT_KEY, subreddit)
            val fragment = PostsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    interface ToolBarInterface {
        fun updateToolBar(subreddit: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_posts, container, false)
        subreddit = arguments?.getString(getString(R.string.subredditKey)) ?: getString(R.string.pics_subreddit)
        if(userVisibleHint){
            val listener = activity as ToolBarInterface?
            listener?.updateToolBar(subreddit)
        }

        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PostViewModel(subreddit, "top", null) as T
            }
        })[PostViewModel::class.java]

        prefs = SharedPrefHelper.defaultPrefs(context!!)
        setUpAdapter()
        trackStatus()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed){
            val listener = activity as ToolBarInterface?
            listener?.updateToolBar(subreddit)
            val layoutPref : String? = prefs["layout"]
            if(currentLayout != layoutPref) {
                changeLayout(false)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun openCommentsView(postId : String) {
        showCommentsPopUp(postId)
    }

    override fun openImageView(postUrl : String, post : RedditPostResponse.Post) {
        showImagePopUp(postUrl, post)
    }

    private fun setUpAdapter() {
        postAdapter = PostAdapter(context!!, this)
        val key = resources.getString(R.string.layoutPrefKey)
        val linearValue = resources.getString(R.string.layoutPrefLinearValue)
        val stagValue = resources.getString(R.string.layoutPrefStagValue)
        recyclerView.apply{
            setHasFixedSize(true)
            currentLayout = prefs[key, linearValue]!!
            if(currentLayout == linearValue){
                layoutManager = LinearLayoutManager(activity)
            }
            else if (currentLayout == stagValue){
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            }
            postAdapter.setLayout(currentLayout)
            adapter = postAdapter
        }

        viewModel.postList.observe(this, Observer {
            postAdapter.submitList(it)
        })
    }

    private fun trackStatus() {
        errorRetryTextView.setOnClickListener { viewModel.retry() }
        viewModel.getStatus().observe(this, Observer { status ->
            if (!viewModel.listIsEmpty() && status == Status.FAILED){
                val snackBar = Snackbar.make(viewOfLayout, resources.getString(R.string.connection_error_description),
                    Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(resources.getString(R.string.reload)){viewModel.retry()}
                snackBar.show()
            }
            progressBar.visibility = if (status == Status.SUCCESS) View.GONE else View.VISIBLE
            errorRetryTextView.visibility = if (viewModel.listIsEmpty() && status == Status.FAILED) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                postAdapter.setStatus(status ?: Status.SUCCESS)
            }
        })
    }

    private fun showCommentsPopUp(postId : String){
        CommentsPopUpView.createPopUpWindow(context!!, this, viewOfLayout, subreddit, postId)
        commentsPopUpOpened = true
        declarePopUpStatus(true)
    }

    private fun showImagePopUp(postUrl : String, post : RedditPostResponse.Post){
        ImagePopUpView.createPopUpWindow(context!!, viewOfLayout, postUrl, post)
    }

    fun closeComments() {
        if (commentsPopUpOpened){
            commentsPopUpOpened = false
            CommentsPopUpView.close()
        }
        declarePopUpStatus(false)
    }

    fun declarePopUpStatus(status : Boolean){
        val parentFrag = activity?.supportFragmentManager?.findFragmentById(R.id.fragment)
        if(parentFrag is SubredditsFragment){
            parentFrag.onClick(status)
        }
    }

    override fun changeLayout(shouldChangePref : Boolean) {
        val key = resources.getString(R.string.layoutPrefKey)
        val linearValue = resources.getString(R.string.layoutPrefLinearValue)
        val stagValue = resources.getString(R.string.layoutPrefStagValue)

        if(currentLayout == linearValue) {
            if(shouldChangePref) {
                prefs[key] = stagValue
            }
            currentLayout = stagValue
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        else if(currentLayout == stagValue) {
            if(shouldChangePref) {
                prefs[key] = linearValue
            }
            currentLayout = linearValue
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        postAdapter.setLayout(currentLayout)
    }

    override fun changeSortBy(sortBy : String, freq : String?) {
        viewModel.changeSortOrder(sortBy, freq)
        viewModel.postList.observe(this, Observer {
            postAdapter.submitList(it)
        })
    }
}
