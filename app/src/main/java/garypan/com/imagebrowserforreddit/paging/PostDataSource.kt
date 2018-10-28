package garypan.com.imagebrowserforreddit.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import garypan.com.imagebrowserforreddit.interfaces.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.disposables.CompositeDisposable

class PostDataSource (private val redditApi : RedditApi,
                      private val disposables : CompositeDisposable,
                      subreddit : String)
                    : ItemKeyedDataSource<String, RedditPostResponse.Children>() {

    var status: MutableLiveData<Status> = MutableLiveData()
    val url = "https://www.reddit.com${subreddit}.json"

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<RedditPostResponse.Children>) {
    }

    override fun loadInitial(params: LoadInitialParams<String>,
                             callback: LoadInitialCallback<RedditPostResponse.Children>) {
        disposables.add(
            redditApi.getInitialPosts(url, "100", "top")
                .subscribe(
                    { response ->
                        status.postValue(Status.SUCCESS)
                        callback.onResult(response.data.children)
                    },
                    {
                        status.postValue(Status.FAILED)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<String>,
                           callback: LoadCallback<RedditPostResponse.Children>) {
        disposables.add(
            redditApi.getNextPosts(url, "100", "top", params.key)
                .subscribe(
                    { response ->
                        status.postValue(Status.SUCCESS)
                        callback.onResult(response.data.children)
                    },
                    {
                        status.postValue(Status.FAILED)
                    }
                )
        )
    }

    override fun getKey(post : RedditPostResponse.Children): String {
        return post.data.name
    }
}