package garypan.com.imagebrowserforreddit.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.fragments.PostsFragment
import kotlinx.android.synthetic.main.activity_main.*
import garypan.com.imagebrowserforreddit.fragments.SubredditsFragment
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper.get


class MainActivity : AppCompatActivity(), PostsFragment.ToolBarInterface {
    companion object {
        private const val TIME_INTERVAL = 2000
        private var mBackPressed: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        setLayoutButton(menu.findItem(R.id.action_change_layout))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // launch settings, fragment
                true
            }
            R.id.action_close -> {
                finish()
                true
            }

            R.id.action_change_layout -> {
                val navHost = supportFragmentManager.findFragmentById(R.id.navigationHost)
                val currentFrag = navHost?.childFragmentManager?.fragments?.get(0)
                if (currentFrag is SubredditsFragment) {
                        currentFrag.changeLayout()
                }
                setLayoutButton(item)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.navigationHost)
        val currentFrag = navHost?.childFragmentManager?.fragments?.get(0)
        if (currentFrag is SubredditsFragment && currentFrag.popupWindowIsOpened) {
            Log.d("fdas", "BackBut Test - frag open")
            currentFrag.onBackPressed()
            return
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            this.finish()
            return
        }
        else {
            Toast.makeText(this, "Tap BACK Again To Exit", Toast.LENGTH_SHORT).show()
        }

        mBackPressed = System.currentTimeMillis()
    }

    override fun updateToolBar(subreddit: String) {
        supportActionBar?.title = subreddit
    }

    private fun setLayoutButton(item: MenuItem){
        val prefs = SharedPrefHelper.defaultPrefs(this)
        val key = resources.getString(R.string.layoutPrefKey)
        val linearValue = resources.getString(R.string.layoutPrefLinearValue)
        if(prefs[key, linearValue] == linearValue) {
            item.setIcon(R.drawable.ic_staggered_view)
        }
        else{
            item.setIcon(R.drawable.ic_linear_view)
        }
    }
}
