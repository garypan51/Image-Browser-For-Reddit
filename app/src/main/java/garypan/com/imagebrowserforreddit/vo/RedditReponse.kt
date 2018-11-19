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
                     val permalink : String,
                     val id : String,
                     val score : Int,
                     val thumbnail_height : Int,
                     val isVideo : Boolean,
                     val distinguished : String,
                     val subreddit : String,
                     val name : String,
                     val preview : Preview,
                     val gildings : Gilding ) {
        fun containsImages() : Boolean{
            var containsImage = false
            preview?.let { containsImage = true
                containsImage = !isVideo}

            distinguished?.let { containsImage = false }

            return containsImage
        }
    }

    data class Gilding( val gid_1 : Int,
                        val gid_2 : Int,
                        val gid_3 : Int )
    {
        fun anyGilds() : Boolean{
            return gid_1 + gid_2 + gid_3 > 0
        }

        fun awardedSilver() : Boolean{
            return gid_1 > 0
        }

        fun awardedGold() : Boolean{
            return gid_2 > 0
        }

        fun awardedPlat() : Boolean{
            return gid_3 > 0
        }
    }

    data class Preview( val images : List<PreviewItems> )

    data class PreviewItems( val  source: Source,
                             val resolutions : List<Source>)

    data class Source( val url : String,
                       val width : Int,
                       val height : Int )
}

object RedditCommentResponse {
    data class Result( val data : Data )

    data class Data( val children : ArrayList<Children>,
                     val after : String,
                     val before : String )

    data class Children( val data : Comment )

    data class Comment( val body : String,
                        val score : Int,
                        val gilded : Int,
                        val author : String,
                        val name : String,
                        val id : String )
}
