package models

import Exts.rarbgTimestamp
import enums.Category
import enums.SubCategory
import repositories.RarbgRepository
import java.util.*

data class Rarbg(
    override val id: String = UUID.randomUUID().toString(),
    override val category: Category? = null,
    override val subCategory: SubCategory? = null,
    override val url: String = "",
    override val filename: String = "",
    override val commentsCount: Int = 0,
    override val elapsedTimestamp: Long = 0,
    override val size: String = "",
    override val completions: Int = 0,
    override val seeders: Int = 0,
    override val leechers: Int = 0,
    override val domain: String = "rarbgmirror.com"
) : Torrent() {

    companion object {

        fun fromHtml(html: String, category: Category? = null, subCategory: SubCategory? = null): Rarbg {
            return Rarbg(
                url = RarbgRepository.domain + RarbgHtmlRegex.URL.regex.find(html)?.groupValues?.last(),
                filename = RarbgHtmlRegex.FILENAME.regex.find(html)?.groupValues?.last() ?: "",
                commentsCount = RarbgHtmlRegex.COMMENTS.regex.find(html)?.groupValues?.last()?.toInt() ?: 0,
                elapsedTimestamp = RarbgHtmlRegex.DATE.regex.find(html)?.groupValues?.last()?.rarbgTimestamp() ?: 0,
                seeders = RarbgHtmlRegex.SEEDERS.regex.find(html)?.groupValues?.last()?.toInt() ?: 0,
                leechers = RarbgHtmlRegex.LEECHERS.regex.find(html)?.groupValues?.last()?.toInt() ?: 0
            )
        }
    }
}

enum class RarbgHtmlRegex(val regex: Regex) {
    URL(Regex("href=\"(.+)\" title=\"")),
    FILENAME(Regex("title=\"(.+?)\">.+?<\\/a>")),
    COMMENTS(Regex("#comments\">(\\d+)<\\/a>")),
    DATE(Regex("\\d{4}-\\d{2}-\\d{2}[ ][\\d{2}:]+")),
    SEEDERS(Regex(">(\\d*)<\\/font>")),
    LEECHERS(Regex(">(\\d+)<\\/td>"))
}