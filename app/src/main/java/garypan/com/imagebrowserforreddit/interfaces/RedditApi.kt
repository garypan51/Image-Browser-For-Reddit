package garypan.com.imagebrowserforreddit.interfaces

import garypan.com.imagebrowserforreddit.vo.RedditCommentResponse
import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RedditApi {
    @GET("/r/{subreddit}/{sort}.json")
    fun getInitialPosts(@Path("subreddit") subreddit : String,
                        @Path("sort") sort: String,
                        @Query("limit") limit: Int) :
            Observable<RedditPostResponse.Result>

    @GET("/r/{subreddit}/{sort}.json")
    fun getNextPosts(@Path("subreddit") subreddit : String,
                     @Path("sort") sort: String,
                     @Query("limit") limit: Int,
                     @Query("after") after: String) :
            Observable<RedditPostResponse.Result>

    @GET("/r/{subreddit}/comments/{postId}.json")
    fun getInitialComments(@Path("subreddit") subreddit : String,
                           @Path("postId") postId : String,
                           @Query("sort") sort: String,
                           @Query("limit") limit: Int) :
            Observable<List<RedditCommentResponse.Result>>

    @GET("/r/{subreddit}/comments/{postId}.json")
    fun getNextComments(@Path("subreddit") subreddit : String,
                        @Path("postId") postId : String,
                        @Query("sort") sort: String,
                        @Query("limit") limit: Int,
                        @Query("after") after: String) :
            Observable<List<RedditCommentResponse.Result>>

    companion object {
        fun create(): RedditApi {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.reddit.com")
                .build()
            return retrofit.create(RedditApi::class.java)
        }
    }
}