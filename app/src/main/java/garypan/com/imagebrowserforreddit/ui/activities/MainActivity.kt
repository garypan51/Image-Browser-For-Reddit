package garypan.com.imagebrowserforreddit.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.base.template.BaseActivity
import garypan.com.imagebrowserforreddit.ui.fragments.posts.PostsFragment
import garypan.com.imagebrowserforreddit.ui.fragments.preferences.PreferenceFragment
import kotlinx.android.synthetic.main.activity_main.*
import garypan.com.imagebrowserforreddit.ui.fragments.subreddits.SubredditsFragment
import garypan.com.imagebrowserforreddit.base.utils.SharedPreferenceHelper
import garypan.com.imagebrowserforreddit.base.utils.SharedPreferenceHelper.get

// TODO MOVE SOME LOGIC TO BASE ACTIVITY LIKE TOOLBAR ACTIONS
class MainActivity : BaseActivity(), PostsFragment.ToolBarInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val prefManager = SharedPreferenceHelper.defaultPrefs(this)
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
                fragmentTransaction.replace(R.id.fragment,
                    PreferenceFragment()
                )
                    .addToBackStack(null)
                    .commit()
                true
            }

            R.id.action_change_layout -> {
                val currentFrag = supportFragmentManager.findFragmentById(R.id.fragment)
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

    override fun updateToolBar(subreddit: String) {
        supportActionBar?.title = subreddit
    }

    private fun setLayoutButton(item: MenuItem){
        val prefManager = SharedPreferenceHelper.defaultPrefs(this)
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