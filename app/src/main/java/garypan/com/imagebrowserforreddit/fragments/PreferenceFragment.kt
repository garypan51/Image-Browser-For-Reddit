package garypan.com.imagebrowserforreddit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import garypan.com.imagebrowserforreddit.R
import androidx.annotation.ColorInt
import android.util.TypedValue
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper
import garypan.com.imagebrowserforreddit.utils.SharedPrefHelper.set

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefManager = SharedPrefHelper.defaultPrefs(context!!)
        val lightModePref = findPreference<Preference>("lightMode") as SwitchPreference
        val acknowledgmentPref = findPreference<Preference>("libraryPref")

        lightModePref.setOnPreferenceChangeListener{_, newValue ->
            val switchedTo = java.lang.Boolean.valueOf(newValue.toString())
            val themePrefKey = resources.getString(R.string.themePrefKey)
            prefManager[themePrefKey] =  switchedTo
            val intent = activity?.intent
            activity?.finish()
            startActivity(intent)
            true
        }

        acknowledgmentPref?.setOnPreferenceClickListener{
            val libraryKey = resources.getString(R.string.libraryKey)
            val licenseFragment = TextPreferenceFragment.newInstance(libraryKey)
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment, licenseFragment)
                ?.addToBackStack(null)
                ?.commit()
                true
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.backgroundColor, typedValue, true)
        @ColorInt val backgroundColor = typedValue.data
        view?.setBackgroundColor(backgroundColor)
        return view
    }
}
