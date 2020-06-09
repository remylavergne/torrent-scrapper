package models

import enums.Category
import enums.SubCategory

abstract class Torrent {
    abstract val id: String
    abstract val category: Category?
    abstract val subCategory: SubCategory?
    abstract val url: String
    abstract val filename: String
    abstract val commentsCount: Int
    abstract val elapsedTimestamp: Long
    abstract val size: String
    abstract val completions: Int
    abstract val seeders: Int
    abstract val leechers: Int
    abstract val domain: String
}
