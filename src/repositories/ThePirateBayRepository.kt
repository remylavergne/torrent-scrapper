package repositories

import models.THEPIRATEBAY_URL
import models.Torrent

object ThePirateBayRepository : BaseRepository() {

    override val name: String = "ThePirateBay"
    override val domain: String
        get() = ""
    private val cookies: String = ""

    override suspend fun search(request: String): List<Torrent> {
        val getCookies = makeRequest("https://thepiratebay.org/", "")

        val response = makeRequest("https://thepiratebay.org/search/lion%20king/0/99/0", "")

        return emptyList()
    }

    override suspend fun checkServerStatus(): Boolean {
        return makeRequest(THEPIRATEBAY_URL, "").code == 200
    }
}