package garypan.com.imagebrowserforreddit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import garypan.com.imagebrowserforreddit.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import garypan.com.imagebrowserforreddit.helpers.RedditRequestManager
import kotlinx.android.synthetic.main.fragment_subreddit.*


class SubredditFragment : Fragment() {
    private lateinit var subredditPath : String
    private lateinit var viewOfLayout: View
    val redditRequestManager = RedditRequestManager()
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
        setUpRV()
        redditRequestManager.fetchPosts(subredditPath, activity!!, recyclerView)
    }


    private fun setUpRV(){
        recyclerView.apply{
            setHasFixedSize(true)
            layoutManager = FlexboxLayoutManager(activity)

            // Uncomment to use Staggered Layout instead of Flexbox
//            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView : RecyclerView , newState : Int){
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        redditRequestManager.fetchPosts(subredditPath, activity!!, recyclerView, redditRequestManager.after)
                    }
                }
            })
        }
    }
}