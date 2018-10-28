package garypan.com.imagebrowserforreddit.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import garypan.com.imagebrowserforreddit.interfaces.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.disposables.CompositeDisposable

class PostViewModel(subreddit : String) : ViewModel() {
    private val redditApiService by lazy {
            RedditApi.create()
    }
    private val disposables = CompositeDisposable()
    var postList: LiveData<PagedList<RedditPostResponse.Children>>
    private val pageSize = 15
    private val postDataSourceFactory : PostDataSourceFactory

    init {
        postDataSourceFactory = PostDataSourceFactory(redditApiService, disposables, subreddit)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        postList = LivePagedListBuilder<String, RedditPostResponse.Children>(postDataSourceFactory, config).build()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}