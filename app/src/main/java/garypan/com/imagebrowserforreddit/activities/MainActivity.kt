package garypan.com.imagebrowserforreddit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.fragments.PostsFragment
import garypan.com.imagebrowserforreddit.fragments.PreferenceFragment
import kotlinx.android.synthetic.main.activity_main.*
import garypan.com.imagebrowserforreddit.fragments.SubredditsFragment
import garypan.com.imagebrowserforreddit.fragments.TextPreferenceFragment
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

        val prefManager = SharedPrefHelper.defaultPrefs(this)
        val themePrefKey = resources.getString(R.string.themePrefKey)

        val enableLight = prefManager[themePrefKey, false]
        if (enableLight!!) {
            setTheme(R.style.LightTheme)
        } else {
            setTheme(R.style.DarkTheme)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        setLayoutButton(menu.findItem(R.id.action_change_layout))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment, PreferenceFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }

            R.id.action_change_layout -> {
                val currentFrag = supportFragmentManager.findFragmentById(R.id.fragment)
//                val currentFrag = navHost?.childFragmentManager?.fragments?.get(0)
                if (currentFrag is SubredditsFragment) {
                    currentFrag.changeLayout(true)
                }
                setLayoutButton(item)
                true
            }

            R.id.sort_top_hour -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_hour))
                true
            }

            R.id.sort_top_day -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_day))
                true
            }

            R.id.sort_top_week -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_week))
                true
            }

            R.id.sort_top_month -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_month))
                true
            }

            R.id.sort_top_year -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_year))
                true
            }

            R.id.sort_top_all_time -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_top), resources.getString(R.string.freq_all_time))
                true
            }

            R.id.sort_controversial_hour -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_hour))
                true
            }

            R.id.sort_controversial_day -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_day))
                true
            }

            R.id.sort_controversial_week -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_week))
                true
            }

            R.id.sort_controversial_month -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_month))
                true
            }

            R.id.sort_controversial_year -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_year))
                true
            }

            R.id.sort_controversial_all_time -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_controversial), resources.getString(R.string.freq_all_time))
                true
            }

            R.id.sort_hot -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_hot), null)
                true
            }

            R.id.sort_rising -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_rising), null)
                true
            }

            R.id.sort_new -> {
                changeRecyclerViewSortOrder(resources.getString(R.string.sort_new), null)
                true
            }

            R.id.action_close -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val currentFrag = supportFragmentManager.findFragmentById(R.id.fragment)

        if (currentFrag is SubredditsFragment && currentFrag.popupWindowIsOpened) {
            currentFrag.onBackPressed()
        }

        else if(currentFrag is PreferenceFragment || currentFrag is TextPreferenceFragment) {
            super.onBackPressed()
        }

        else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                this.finish()
                return
            } else {
                Toast.makeText(this, "Tap BACK Again To Exit", Toast.LENGTH_SHORT).show()
            }

            mBackPressed = System.currentTimeMillis()
        }
    }

    override fun updateToolBar(subreddit: String) {
        supportActionBar?.title = subreddit
    }

    private fun setLayoutButton(item: MenuItem){
        val prefManager = SharedPrefHelper.defaultPrefs(this)
        val key = resources.getString(R.string.layoutPrefKey)
        val linearValue = resources.getString(R.string.layoutPrefLinearValue)
        if(prefManager[key, linearValue] == linearValue) {
            item.setIcon(R.drawable.ic_staggered_view)
        }
        else{
            item.setIcon(R.drawable.ic_linear_view)
        }
    }

    private fun changeRecyclerViewSortOrder(sortBy : String, freq : String?){
        val currentFrag = supportFragmentManager.findFragmentById(R.id.fragment)
        if (currentFrag is SubredditsFragment) {
            currentFrag.changeSortBy(sortBy, freq)
        }
    }

}
