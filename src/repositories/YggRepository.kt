package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Torrent
import models.YGG_URL
import models.YggTorrent
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object YggRepository : BaseRepository() {

    override val name: String = "YggTorrent"
    override val domain: String
        get() = ""

    override suspend fun search(request: String): List<Torrent> {
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<YggTorrent>()

            try {
                Jsoup.connect(
                    "https://www2.yggtorrent.pe/engine/search?name=${request.replace(
                        " ",
                        "+"
                    )}&description=&file=&uploader=&category=all&sub_category=&do=search&order=desc&sort=publish_date"
                ).timeout(10_000).get()
                    .run {

                        val elements = this.getElementsByClass("table-responsive results")

                        if (elements.isNotEmpty()) {
                            // Get Node who hold every results
                            val childNodes = elements.first().childNodes().filterIsInstance<Element>()
                                .first().childNodes().filterIsInstance<Element>().first { it.tagName() == "tbody" }
                                .childNodes().filterIsInstance<Element>()

                            // Extract all objects found
                            childNodes.forEach { element ->
                                val tempElement = element.childNodes().filterIsInstance<Element>()

                                val informations = mutableListOf<String>()

                                tempElement.forEach { subElement ->
                                    informations.add(subElement.toString().replace("\n", ""))
                                }

                                // Create object & Save it
                                val fromListHtml = YggTorrent.fromListHtml(informations)
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

        var status = false
        var cookiesString = ""

        val response = makeRequest(YGG_URL, "")

        // Get cookies
        response.headers.forEach { (key, value) ->
            if (key.toLowerCase() == "set-cookie") {
                cookiesString += "$value; "
            }
        }

        if (response.code == 307) {
            val makeRequest = makeRequest(response.headers["location"]!!.replace("http", "https"), cookiesString)
            if (makeRequest.code == 200) {
                status = true
            }
        }
        return status
    }
}