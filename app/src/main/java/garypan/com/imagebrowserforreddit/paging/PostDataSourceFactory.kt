package garypan.com.imagebrowserforreddit.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import garypan.com.imagebrowserforreddit.utils.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.disposables.CompositeDisposable

class PostDataSourceFactory (private val redditApi : RedditApi,
                             private val disposables : CompositeDisposable,
                             private val subreddit : String,
                             private val sortBy : String,
                             private val freq : String?)
                        : DataSource.Factory<String, RedditPostResponse.Children>() {

    val postDataSourceLiveData = MutableLiveData<PostDataSource>()

    override fun create(): DataSource<String, RedditPostResponse.Children> {
        val postDataSource = PostDataSource(redditApi, disposables, subreddit, sortBy, freq)
        postDataSourceLiveData.postValue(postDataSource)
        return postDataSource
    }
}