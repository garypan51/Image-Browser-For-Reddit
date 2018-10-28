package garypan.com.imagebrowserforreddit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import garypan.com.imagebrowserforreddit.R
import com.google.android.flexbox.FlexboxLayoutManager
import garypan.com.imagebrowserforreddit.adapters.PostAdapter
import garypan.com.imagebrowserforreddit.paging.PostViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_subreddit.*


class SubredditFragment : Fragment() {
    private lateinit var subredditPath : String
    private lateinit var viewOfLayout: View
    private lateinit var viewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter


//    val redditRequestManager = RedditApiManager()
    var disposable: Disposable? = null

    private val TAG = "SubRedFraf"

    companion object {
        val key = "subreddit"

        fun newInstance(subreddit: String): SubredditFragment {
            val args = Bundle()
            args.putSerializable(key, subreddit)
            val fragment = SubredditFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_subreddit, container, false)
        subredditPath = arguments?.getString(getString(R.string.subredditKey)) ?: getString(R.string.pics_subreddit)
        return viewOfLayout
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PostViewModel(subredditPath) as T
            }
        })[PostViewModel::class.java]
        initAdapter()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun initAdapter() {
        postAdapter = PostAdapter(context!!)
        recyclerView.apply{
            setHasFixedSize(true)
            layoutManager = FlexboxLayoutManager(activity)

//            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = postAdapter
        }

        viewModel.postList.observe(this, Observer {
            postAdapter.submitList(it)
        })
    }
}
