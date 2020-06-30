package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Rarbg
import models.Torrent

object RarbgRepository : BaseRepository() {

    override val name: String
        get() = "RARBG"

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val url = "https://www.rarbgproxy.org/torrents.php?search=${request.replace(' ', '+')}"
            val torrents = mutableListOf<Torrent>()

            try {
                getJsoupClient(url).get().run {
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
}