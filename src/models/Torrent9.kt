package models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Torrent9(
    override val id: String = java.util.UUID.randomUUID().toString(),
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

        fun fromListHtml(list: String): Torrent9 {
            if (list.isEmpty()) {
                throw Exception("Wrong list !")
            }

            return Torrent9(
                domain = "Torrent9.is",
                url = TORRENT9_DOMAIN + "${Torrent9HtmlEnums.FILE_DETAILS_URL.regex.find(list)?.groupValues?.last()
                    ?.trim()}",
                filename = Torrent9HtmlEnums.FILENAME.regex.find(list)?.groupValues?.last()?.trim() ?: "No name",
                commentsCount = 0,
                elapsedTimestamp = 0,
                size = Torrent9HtmlEnums.SIZE.regex.find(list)?.groupValues?.last() ?: "0",
                completions = 0,
                seeders = Torrent9HtmlEnums.SEEDERS.regex.find(list)?.groupValues?.last()?.toInt() ?: 0,
                leechers = Torrent9HtmlEnums.LEECHERS.regex.find(list)?.groupValues?.last()?.toInt() ?: 0
            )
        }
    }
}

enum class Torrent9HtmlEnums(val regex: Regex) {
    FILE_DETAILS_URL(Regex("\" href=\"(.+)\" style=")), // OK
    FILENAME(Regex("<a title=\"(.+)\" href")), // OK
    SIZE(Regex("<td style=\"font-size:12px\">(.+?)<\\/td>")), // OK
    SEEDERS(Regex("<span class=\"seed_ok\">(\\d+) <")), // OK
    LEECHERS(Regex("right:5px\">(\\d+) <img")), // OK
}