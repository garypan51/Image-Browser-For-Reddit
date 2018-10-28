package garypan.com.imagebrowserforreddit.interfaces

import garypan.com.imagebrowserforreddit.vo.RedditPostResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RedditApi {
    @GET
    fun getInitialPosts(@Url url : String,
                        @Query("limit") limit: String,
                        @Query("sort") sort: String) :
            Observable<RedditPostResponse.Result>

    @GET
    fun getNextPosts(@Url url : String,
                        @Query("limit") limit: String,
                        @Query("sort") sort: String,
                        @Query("after") after: String):
            Observable<RedditPostResponse.Result>

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