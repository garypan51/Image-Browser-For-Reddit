package garypan.com.imagebrowserforreddit.helpers

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.moshi.Moshi
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.adapters.PostRecyclerViewAdapter
import garypan.com.imagebrowserforreddit.models.Comment
import garypan.com.imagebrowserforreddit.models.Post
import garypan.com.imagebrowserforreddit.models.ShowCommentsEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RedditRequestManager {
    private val TAG = "RedditRequestManager"
    private val redditPosts = ArrayList<Post>()
    var after = ""

    fun fetchComments(subredditPath : String, postId : String, con : Context) : ArrayList<Comment>{
        val url = con.getString(R.string.base_url) + subredditPath + con.getString(R.string.comments) + postId +
                con.getString(R.string.json_format_request)
        val comments = ArrayList<Comment>()

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> {
                try{
                    val postComments  = ((it.getJSONObject(1))).getJSONObject("data").getJSONArray("children")
                    val moshi =  Moshi.Builder().build()
                    val jsonAdapter = moshi.adapter(Comment::class.java)

                    for(i in 0 until postComments.length()){
                        val json : JSONObject = (postComments[i] as JSONObject).getJSONObject("data")
                            val comment = jsonAdapter.fromJson(json.toString())
                            comments.add(comment!!)
                    }
                }
                catch(exception : JSONException){
                    Log.d(TAG, exception.message)
                }

            }, Response.ErrorListener { error -> Log.d(TAG, error.message) })
        VolleyRequestQueue.instance?.addToRequestQueue<JSONArray>(request)
        return comments
    }

    fun fetchPosts(subredditPath : String, con : FragmentActivity, rv : RecyclerView, after : String = "") : Unit{
        // can change to reddit helper class - getURL(reddit, sort, )
        val url = when {
            after.equals("") -> con.getString(R.string.base_url) + subredditPath + con.getString(R.string.json_format_request) +
                    con.getString(R.string.limit) + con.getString(R.string.sorting)
            else -> con.getString(R.string.base_url) + subredditPath + con.getString(R.string.json_format_request) +
                    con.getString(R.string.limit) + con.getString(R.string.sorting) + con.getString(R.string.after) + after
        }

        val request = JsonObjectRequest(
        Request.Method.GET, url, null,
        Response.Listener<JSONObject> {
            try{
                val subRedditPosts : JSONArray = ((it.getJSONObject("data")).getJSONArray("children"))

                val moshi =  Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Post::class.java)

                for(i in 0 until subRedditPosts.length()){
                    val json = (subRedditPosts.getJSONObject(i)).getJSONObject("data")
                    if(!json.isNull("thumbnail_height") && !json.getBoolean("is_video") &&
                        json.isNull("distinguished") && !json.getString("url").contains("v.redd.it")){
                        val post = jsonAdapter.fromJson(json.toString())
                        redditPosts.add(post!!)
                    }
                }
                this.after = (it.getJSONObject("data")).getString("after")
            }
            catch(exception : JSONException){
                Log.d(TAG, exception.message)
            }
            when{
                after.equals("") -> rv.adapter = PostRecyclerViewAdapter(redditPosts, con)
                else -> (rv.adapter as PostRecyclerViewAdapter).addPosts(redditPosts)
            }
        }, Response.ErrorListener { error -> Log.d(TAG, error.message) })
        VolleyRequestQueue.instance?.addToRequestQueue<JSONObject>(request)
    }
}