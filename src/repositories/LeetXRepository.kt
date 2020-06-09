package repositories

import models.LEETX_URL
import models.LeetX
import models.Torrent
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object LeetXRepository : BaseRepository() {

    override val name: String = "1337x"
    override val domain: String
        get() = ""

    override suspend fun search(request: String): List<Torrent> {

        val leetXs = mutableListOf<LeetX>()
        val url = "https://1337x.to/search/${request.replace(" ", "+")}/1/"
        try {
            val response = makeRequest(url, "")
            if (response.code == 200) {
                val body = response.body?.string()
                Jsoup.parse(body).run {
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
            }
        } catch (e: Exception) {
            println()
        }

        return leetXs
    }

    override suspend fun checkServerStatus(): Boolean {
        lateinit var response: Response
        try {
            response = makeRequest(LEETX_URL, "")
        } catch (e: Exception) {
            response.close()
        }
        return response.code == 200
    }
}
