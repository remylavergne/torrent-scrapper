package repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        return withContext(Dispatchers.IO) {
            val leetXs = mutableListOf<LeetX>()
            val url = "https://1337x.to/search/${request.replace(" ", "+")}/1/"

            try {
                Jsoup.connect(url)
                    .timeout(10_000)
                    .proxy("1.255.48.197", 8080)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
                    .get().run {
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
