package models

import enums.Category
import enums.SubCategory
import java.util.*

class YggTorrent(
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
    override val domain: String = ""
) : Torrent() {

    companion object {

        /**
         * Some endpoints are parse from JSON, and some tags are a little bit different
         */
        fun fromListJson(list: List<String>, category: Category? = null, subCategory: SubCategory? = null): YggTorrent {

            if (list.count() != 9) {
                throw Exception("Wrong list !")
            }

            return YggTorrent(
                domain = YggJsonEnums.DOMAIN_NAME.regex.find(list[1])?.groupValues?.last() ?: "No domain",
                url = YggJsonEnums.FILE_DETAILS_URL.regex.find(list[1])?.groupValues?.last() ?: "No url",
                filename = YggJsonEnums.FILENAME.regex.find(list[1])?.groupValues?.last() ?: "No name",
                commentsCount = list[3].toInt(),
                elapsedTimestamp = YggJsonEnums.ELAPSED_TIME.regex.find(list[4])?.groupValues?.last()?.toLong() ?: 0,
                size = YggJsonEnums.FILE_SIZE.regex.find(list[5])?.groupValues?.last() ?: "",
                completions = list[6].toInt(),
                seeders = list[7].toInt(),
                leechers = list[8].toInt()
            )
        }

        fun fromListHtml(list: List<String>, category: Category? = null, subCategory: SubCategory? = null): YggTorrent {
            if (list.count() != 9) {
                throw Exception("Wrong list !")
            }

            return YggTorrent(
                domain = YggHtmlEnums.DOMAIN_NAME.regex.find(list[1])?.groupValues?.last() ?: "No domain",
                url = YggHtmlEnums.FILE_DETAILS_URL.regex.find(list[1])?.groupValues?.last() ?: "No url",
                filename = YggHtmlEnums.FILENAME.regex.find(list[1])?.groupValues?.last()?.trim() ?: "No name",
                commentsCount = YggHtmlEnums.COMMENTS.regex.find(list[3])?.groupValues?.last()?.toInt() ?: 0,
                elapsedTimestamp = YggHtmlEnums.ELAPSED_TIME.regex.find(list[4])?.groupValues?.last()?.toLong() ?: 0,
                size = YggHtmlEnums.BETWEEN_TD.regex.find(list[5])?.groupValues?.last() ?: "0",
                completions = YggHtmlEnums.BETWEEN_TD.regex.find(list[6])?.groupValues?.last()?.toInt() ?: 0,
                seeders = YggHtmlEnums.BETWEEN_TD.regex.find(list[7])?.groupValues?.last()?.toInt() ?: 0,
                leechers = YggHtmlEnums.BETWEEN_TD.regex.find(list[8])?.groupValues?.last()?.toInt() ?: 0
            )
        }
    }
}

enum class YggJsonEnums(val regex: Regex) {
    DOMAIN_NAME(Regex("http[s]*:\\/\\/www\\d*\\.(\\w*\\d*\\.\\w{2,4})")),
    FILE_DETAILS_URL(Regex("=\"(.+)\">")),
    FILENAME(Regex("\">(.+)<\\/a>")),
    ELAPSED_TIME(Regex(">(\\d+)<")),
    FILE_SIZE(Regex(">(\\d+)<"))
}

enum class YggHtmlEnums(val regex: Regex) {
    DOMAIN_NAME(Regex("http[s]*:\\/\\/www\\d*\\.(\\w*\\d*\\.\\w{2,4})")),
    FILE_DETAILS_URL(Regex("href=\"(.+)\">")),
    FILENAME(Regex("\">.+\">(.+)<\\/a>")),
    COMMENTS(Regex("<td>(\\d+)[ ]*<span")),
    ELAPSED_TIME(Regex("class=\"hidden\">[ ]*(\\d+)[ ]*<\\/div>")),
    BETWEEN_TD(Regex("<td>(.+)<\\/td>"))
}

enum class YggEndpoints(val endpoint: String) {
    DAILY("https://www2.yggtorrent.pe/engine/ajax_top_query/day"),
    WEEKLY("https://www2.yggtorrent.pe/engine/ajax_top_query/week"),
    MONTH("https://www2.yggtorrent.pe/engine/ajax_top_query/week"),
}