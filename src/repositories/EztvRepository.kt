package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Eztv
import models.Torrent
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object EztvRepository : BaseRepository() {

    override val name: String
        get() = "Eztv.io"
    override val domain: String
        get() = "https://eztv.io"

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val eztv = mutableListOf<Torrent>()

            val url = "https://eztv.io/search/${request.replace(' ', '-')}"

            try {
                val makeRequest = makeRequest(url, "")

                makeRequest.body?.string()?.let { body ->

                    Jsoup.parse(body).run {

                        val elementsByClass = this.getElementsByClass("forum_header_border")

                        if (elementsByClass.isNotEmpty()) {
                            val filter = elementsByClass.filter { it.tagName() == "tr" }

                            filter.forEach { element ->
                                var temporaryHtml = ""
                                element.childNodes().filterIsInstance<Element>().forEach { informations ->
                                    temporaryHtml += informations.toString()
                                }
                                // All informations necessary to build an object
                                val newObject = Eztv.fromHtml(temporaryHtml)
                                eztv.add(newObject)
                            }
                        }
                    }
                }
                return@withContext eztv
            } catch (e: Exception) {
                println(e)
                return@withContext emptyList<Torrent>()
            }
        }
    }

    override suspend fun checkServerStatus(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}