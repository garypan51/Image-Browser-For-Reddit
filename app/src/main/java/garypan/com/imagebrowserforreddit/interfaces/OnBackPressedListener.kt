package garypan.com.imagebrowserforreddit.interfaces

interface OnBackPressedListener {
    var popupWindowIsOpened : Boolean
    fun onBackPressed()
}