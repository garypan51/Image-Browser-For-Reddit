package garypan.com.imagebrowserforreddit.vo


object RedditPostResponse {
    data class Result( val data : Data )

    data class Data( val children : ArrayList<Children>,
                     val after : String,
                     val before : String )

    data class Children( val data : Post )

    data class Post( val title : String,
                     val author : String,
                     val url : String,
                     val id : String,
                     val thumbnail_height : Int,
                     val isVideo : Boolean,
                     val distinguished : String,
                     val subreddit : String,
                     val name : String,
                     val preview : Preview ) {
        fun containsImages() : Boolean{
            var containsImage : Boolean
            when(preview){
                null -> containsImage = false
                else -> containsImage = true
            }

            when(isVideo){
                true -> containsImage = false
                else -> containsImage = true
            }

            when(distinguished){
                null -> containsImage = true
                else -> containsImage = false
            }
            return containsImage
        }
    }

    data class Preview( val images : List<PreviewItems> )

    data class PreviewItems( val  source: Source )

    data class Source( val url : String,
                       val width : Int,
                       val height : Int)
}

object RedditCommentResponse {
    data class Result(val data : Data)

    data class Data( val children : ArrayList<Children>,
                     val after : String,
                     val before : String )

    data class Children( val data : Comment )

    data class Comment( val body : String,
                        val score : Int,
                        val gilded : Int,
                        val author : String,
                        val name : String,
                        val id : String)
}
