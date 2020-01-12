package garypan.com.imagebrowserforreddit.base.interfaces

interface OnBackPressedListener {
    var popupWindowIsOpened : Boolean
    fun onBackPressed()
}