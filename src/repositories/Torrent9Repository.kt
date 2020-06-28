package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Torrent
import models.Torrent9
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object Torrent9Repository : BaseRepository() {
    override val name: String = "Torrent9"
    override val domain: String = ""

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<Torrent9>()

            try {
                Jsoup.connect(
                    "https://ww1.torrent9.is/search_torrent/${request.replace(" ", "-")}.html"
                ).get()
                    .run {

                        val elements = this.select("tbody")

                        if (elements.isNotEmpty()) {

                            val childNodes = elements.first().childNodes().filterIsInstance<Element>()

                            // Extract all objects found
                            childNodes.forEach { element ->
                                val tempElement = element.childNodes().filterIsInstance<Element>()

                                var informations: String = ""

                                tempElement.forEach { subElement ->
                                    informations += "${subElement.outerHtml()} // "
                                }

                                // Create object & Save it
                                val fromListHtml = Torrent9.fromListHtml(informations)
                                items.add(fromListHtml)
                            }
                        }
                    }
                return@withContext items
            } catch (e: Exception) {
                println(e)
                return@withContext emptyList<Torrent>()
            }
        }
    }

    override suspend fun checkServerStatus(): Boolean {
        TODO("Not yet implemented")
    }
}