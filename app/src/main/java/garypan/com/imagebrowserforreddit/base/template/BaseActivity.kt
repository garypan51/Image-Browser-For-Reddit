package garypan.com.imagebrowserforreddit.base.template

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import garypan.com.imagebrowserforreddit.R
import garypan.com.imagebrowserforreddit.ui.fragments.preferences.PreferenceFragment
import garypan.com.imagebrowserforreddit.ui.fragments.preferences.TextPreferenceFragment
import garypan.com.imagebrowserforreddit.ui.fragments.subreddits.SubredditsFragment

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val TIME_INTERVAL = 2000
        private var mBackPressed: Long = 0
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
}
