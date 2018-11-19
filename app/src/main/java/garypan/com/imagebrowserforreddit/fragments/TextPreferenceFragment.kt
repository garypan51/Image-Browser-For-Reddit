package garypan.com.imagebrowserforreddit.fragments


import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import garypan.com.imagebrowserforreddit.R


class TextPreferenceFragment : Fragment() {

    companion object {
        fun newInstance(type: String): TextPreferenceFragment {
            val args = Bundle()
            args.putString("type", type)
            val fragment = TextPreferenceFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var type : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("type") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_text_preference, container, false)
        val headView = view.findViewById<TextView>(R.id.headerView)
        val bodyView = view.findViewById<TextView>(R.id.bodyView)

        val libraryKey = resources.getString(R.string.libraryKey)
        if(type == libraryKey){
            headView.text = (getString(R.string.libraryMain))
            bodyView.text = (getString(R.string.libraryBody))
        }

        val typedValue = TypedValue()
        val theme = context?.theme
        theme?.resolveAttribute(R.attr.backgroundColor, typedValue, true)
        @ColorInt val backgroundColor = typedValue.data
        view?.setBackgroundColor(backgroundColor)
        return view
    }

}
