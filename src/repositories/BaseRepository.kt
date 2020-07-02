package repositories

import models.Torrent
import org.jsoup.Connection
import org.jsoup.Jsoup

abstract class BaseRepository {

    abstract val name: String

    abstract suspend fun search(request: String): List<Torrent>

    fun getJsoupClient(url: String): Connection = Jsoup.connect(url)
        .timeout(10_000)
        .referrer("https://www.google.com")
        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
        //.proxy("144.76.214.158", 1080) // TODO => Update Proxy Dynamically
}