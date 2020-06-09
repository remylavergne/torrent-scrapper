package repositories

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

        val eztv = mutableListOf<Torrent>()

        val url = "https://eztv.io/search/${request.replace(' ', '-')}"

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

        return eztv
    }

    override suspend fun checkServerStatus(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}