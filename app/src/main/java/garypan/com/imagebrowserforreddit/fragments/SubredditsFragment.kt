package garypan.com.imagebrowserforreddit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.adapters.ViewPagerAdapter
import garypan.com.imagebrowserforreddit.interfaces.OnBackPressedListener
import kotlinx.android.synthetic.main.fragment_subreddits.*
import androidx.viewpager.widget.ViewPager
import com.rd.animation.type.AnimationType
import garypan.com.imagebrowserforreddit.interfaces.OnRecyclerViewChangedListener

class SubredditsFragment : Fragment(), OnBackPressedListener, OnRecyclerViewChangedListener{
    private lateinit var homeAdapter : ViewPagerAdapter
    private var currentPostFragment : PostsFragment? = null
    override var popupWindowIsOpened = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subreddits, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeAdapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        addPictureSubreddits(homeAdapter)
        viewPager.adapter = homeAdapter
        indicator.setViewPager(viewPager)
        indicator.setAnimationType(AnimationType.DROP)

        val onChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                currentPostFragment = homeAdapter.getFragment(position) as PostsFragment
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }
        viewPager.addOnPageChangeListener(onChangeListener)
        onChangeListener.onPageSelected(0)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        if(currentPostFragment != null && currentPostFragment is PostsFragment){
            currentPostFragment?.closeComments()
        }
    }

    override fun changeLayout(shouldChangePref : Boolean){
        if(currentPostFragment != null && currentPostFragment is PostsFragment){
            currentPostFragment?.changeLayout(shouldChangePref)
        }
    }

    override fun changeSortBy(sortBy : String, freq : String?) {
        if(currentPostFragment != null && currentPostFragment is PostsFragment){
            currentPostFragment?.changeSortBy(sortBy, freq)
        }
    }

    fun addPictureSubreddits(adapter : ViewPagerAdapter){
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.pics_subreddit)))
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.mildlyinteresting_subreddit)))
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.aww_subreddit)))
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.funny_subreddit)))
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.dog_pictures_subreddit)))
        adapter.addFragment(PostsFragment.newInstance(getString(R.string.puppy_smiles_subreddit)))
    }

    fun onClick(isOpened: Boolean) {
        popupWindowIsOpened = isOpened
    }
}
