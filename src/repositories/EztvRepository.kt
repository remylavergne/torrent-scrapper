package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Eztv
import models.Torrent
import org.jsoup.nodes.Element

object EztvRepository : BaseRepository() {

    override val name: String
        get() = "Eztv.io"

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val eztv = mutableListOf<Torrent>()

            val url = "https://eztv.io/search/${request.replace(' ', '-')}"

            try {
                getJsoupClient(url).get().run {
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
                return@withContext clearIrrelevantEztvResults(request, eztv)
            } catch (e: Exception) {
                println(e)
                return@withContext emptyList<Torrent>()
            }
        }
    }

    /**
     * If EZTV found nothing relevant, it displays some random results. We don't need to expose them to the client...
     */
    private fun clearIrrelevantEztvResults(request: String, founds: List<Torrent>): List<Torrent> {
        val words = request.split(" ")
        val resultsFiltered = mutableListOf<Torrent>()

        founds.forEach { torrent: Torrent ->
            words.forEach searchWordMatching@{ word: String ->
                if (torrent.filename.contains(word)) {
                    resultsFiltered.add(torrent)
                    return@searchWordMatching
                }
            }
        }
        return resultsFiltered
    }
}