package garypan.com.imagebrowserforreddit.models

class Post( val title : String,
            val author : String,
            val url : String,
            val id : String,
            val subreddit : String,
            val preview : Preview)

class Preview( val images : List<PreviewItems>)

class PreviewItems( val  source: Source )

class Source( val url : String,
              val width : Int,
              val height : Int)