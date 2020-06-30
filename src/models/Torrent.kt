package models

import com.squareup.moshi.JsonClass
import enums.Category
import enums.SubCategory

@JsonClass(generateAdapter = true)
open class Torrent {
    open val id: String = ""
    open val category: Category? = null
    open val subCategory: SubCategory? = null
    open val url: String = ""
    open val filename: String = ""
    open val commentsCount: Int = 0
    open val elapsedTimestamp: Long = 0L
    open val size: String = ""
    open val completions: Int = 0
    open val seeders: Int = 0
    open val leechers: Int = 0
    open val domain: String = ""
}
