package garypan.com.imagebrowserforreddit.helpers

import android.app.Application
import com.android.volley.Request
import com.android.volley.toolbox.Volley

class VolleyRequestQueue : Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @get:Synchronized var instance: VolleyRequestQueue? = null
    }

    val requestQueue by lazy { Volley.newRequestQueue(applicationContext) }

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue?.add(request)
    }
}