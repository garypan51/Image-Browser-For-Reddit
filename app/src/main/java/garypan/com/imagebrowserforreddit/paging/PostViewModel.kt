package garypan.com.imagebrowserforreddit.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import garypan.com.imagebrowserforreddit.utils.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.disposables.CompositeDisposable

class PostViewModel(private val subreddit : String, sortBy : String, freq: String?) : ViewModel() {
    private val redditApiService by lazy {
            RedditApi.create()
    }
    private val disposables = CompositeDisposable()
    var postList: LiveData<PagedList<RedditPostResponse.Children>>
    private val pageSize = 15
    private var postDataSourceFactory : PostDataSourceFactory

    init {
        postDataSourceFactory = PostDataSourceFactory(redditApiService, disposables, subreddit, sortBy, freq)
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

    fun getStatus(): LiveData<Status> = Transformations.switchMap<PostDataSource,
            Status>(postDataSourceFactory.postDataSourceLiveData, PostDataSource::status)

    fun retry() {
        postDataSourceFactory.postDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return postList.value?.isEmpty() ?: true
    }

    fun changeSortOrder(sortBy : String, freq : String?){
        onCleared()
        postDataSourceFactory = PostDataSourceFactory(redditApiService, disposables, subreddit, sortBy, freq)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        postList = LivePagedListBuilder<String, RedditPostResponse.Children>(postDataSourceFactory, config).build()
    }


}