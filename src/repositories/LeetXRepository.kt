package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.LeetX
import models.Torrent
import org.jsoup.nodes.Element

object LeetXRepository : BaseRepository() {

    override val name: String = "1337x"

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val leetXs = mutableListOf<LeetX>()
            val url = "https://www.1337x.to/search/${request.replace(" ", "+")}/1/"

            try {
                getJsoupClient(url).get().run {
                    // Object
                    val elementsByClass = this.getElementsByClass("table-list")
                    if (elementsByClass.isNotEmpty()) {
                        val elements = elementsByClass.first().childNodes().filterIsInstance<Element>()
                            .first { it.tag().normalName() == "tbody" }
                            .childNodes().filterIsInstance<Element>()

                        elements.forEach { element ->
                            val tempList = mutableListOf<String>()
                            // Elements information extraction here
                            element.childNodes().filterIsInstance<Element>().forEach {
                                // Store in a listOf<String>()
                                tempList.add(it.toString().replace("\n", ""))
                            }
                            // Create a true LeetX object and store it
                            leetXs.add(LeetX.fromHtml(tempList))
                        }
                    }
                }
                return@withContext leetXs
            } catch (e: Exception) {
                println(e)
                return@withContext emptyList<Torrent>()
            }
        }
    }
}
