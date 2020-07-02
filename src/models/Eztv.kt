package models

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Eztv(
    override val id: String = UUID.randomUUID().toString(),
    override val url: String = "",
    override val filename: String = "",
    override val commentsCount: Int = 0,
    override val elapsedTimestamp: Long = 0,
    override val size: String = "",
    override val completions: Int = 0,
    override val seeders: Int = 0,
    override val leechers: Int = 0,
    override val domain: String = "Eztv.io"
) : Torrent() {

    companion object {
        fun fromHtml(html: String): Eztv {
            return Eztv(
                url = EZTV_DOMAIN + EztvRegex.URL.regex.find(html)?.groupValues?.last(),
                filename = EztvRegex.FILENAME.regex.find(html)?.groupValues?.last() ?: "",
                elapsedTimestamp = System.currentTimeMillis(),
                seeders = EztvRegex.SEEDERS.regex.find(html)?.groupValues?.last()?.toInt() ?: 0
            )
        }
    }
}

enum class EztvRegex(val regex: Regex) {
    URL(Regex("<a href=\"(\\/ep\\/.+?)\" title=\"")),
    FILENAME(Regex(">([A-Z|0-9][\\w -]+\\[eztv\\])<\\/a>")),
    SEEDERS(Regex("<font color=\"green\">(\\d*)<\\/font>"))
}
