package garypan.com.imagebrowserforreddit.interfaces

interface OnRecyclerViewChangedListener {
    fun changeLayout(shouldChangePref : Boolean)
    fun changeSortBy(sortBy : String, freq : String?)
}