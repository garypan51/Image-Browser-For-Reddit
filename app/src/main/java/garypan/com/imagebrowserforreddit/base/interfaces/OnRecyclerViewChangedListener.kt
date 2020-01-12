package garypan.com.imagebrowserforreddit.base.interfaces

interface OnRecyclerViewChangedListener {
    fun changeLayout(shouldChangePref : Boolean)
    fun changeSortBy(sortBy : String, freq : String?)
}