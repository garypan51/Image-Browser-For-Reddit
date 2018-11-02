package garypan.com.imagebrowserforreddit.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import garypan.com.imagebrowserforreddit.interfaces.RedditApi
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class PostDataSource (private val redditApi : RedditApi,
                      private val disposables : CompositeDisposable,
                      private val subreddit : String)
                    : ItemKeyedDataSource<String, RedditPostResponse.Children>() {

    var status: MutableLiveData<Status> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<RedditPostResponse.Children>) {
    }

    override fun loadInitial(params: LoadInitialParams<String>,
                             callback: LoadInitialCallback<RedditPostResponse.Children>) {
        status.postValue(Status.RUNNING)
        disposables.add(
            redditApi.getInitialPosts(subreddit, "top", 100)
                .subscribe(
                    { response ->
                        status.postValue(Status.SUCCESS)
                        callback.onResult(response.data.children)
                    },
                    {
                        status.postValue(Status.FAILED)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<String>,
                           callback: LoadCallback<RedditPostResponse.Children>) {
        status.postValue(Status.RUNNING)
        disposables.add(
            redditApi.getNextPosts(subreddit, "top", 100, params.key)
                .subscribe(
                    { response ->
                        status.postValue(Status.SUCCESS)
                        callback.onResult(response.data.children)
                    },
                    {
                        status.postValue(Status.FAILED)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun getKey(post : RedditPostResponse.Children): String {
        return post.data.name
    }

    fun retry() {
        if (retryCompletable != null) {
            disposables.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}