package garypan.com.imagebrowserforreddit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.adapters.ViewPagerAdapter
import garypan.com.imagebrowserforreddit.fragments.SubredditFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val fragmentManager = supportFragmentManager
        val homeAdapter = ViewPagerAdapter(fragmentManager)
        addPictureSubreddits(homeAdapter)

        viewPager.apply {
            adapter = homeAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    when(position){
                        0 -> supportActionBar?.title = getString(R.string.pics_subreddit)
                        1 -> supportActionBar?.title = getString(R.string.aww_subreddit)
                        2 -> supportActionBar?.title = getString(R.string.funny_subreddit)
                        3 -> supportActionBar?.title = getString(R.string.mildlyinteresting_subreddit)
                        4 -> supportActionBar?.title = getString(R.string.dog_pictures_subreddit)
                        5 -> supportActionBar?.title = getString(R.string.puppy_smiles_subreddit)
                    }
                }
            })
        }
        indicator.setViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addPictureSubreddits(adapter : ViewPagerAdapter){
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.pics_subreddit)))
        supportActionBar?.title = getString(R.string.pics_subreddit)
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.aww_subreddit)))
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.funny_subreddit)))
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.mildlyinteresting_subreddit)))
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.dog_pictures_subreddit)))
        adapter.addFragment(SubredditFragment.newInstance(getString(R.string.puppy_smiles_subreddit)))
    }
}
