package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Rarbg
import models.Torrent
import okhttp3.Response
import org.jsoup.Jsoup

object RarbgRepository : BaseRepository() {

    override val name: String
        get() = "RARBG"
    override val domain: String = "https://rarbgmirror.com"

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val url = "https://rarbgmirror.com/torrents.php?search=${request.replace(' ', '+')}&order=seeders&by=DESC"
            var response: Response? = null
            val torrents = mutableListOf<Torrent>()

            try {
                response = makeRequest(url, "")
                Jsoup.parse(response?.body?.string()).run {

                    val elementsByClass = this.getElementsByClass("lista2")

                    if (elementsByClass.isNotEmpty()) {
                        elementsByClass.forEach { element ->
                            val html = element.toString()
                            val newElement = Rarbg.fromHtml(html)
                            torrents.add(newElement)
                        }
                    }
                }
                return@withContext torrents
            } catch (e: Exception) {
                println(e)
                return@withContext emptyList<Torrent>()
            }
        }
    }

    override suspend fun checkServerStatus(): Boolean {
        return false
    }
}