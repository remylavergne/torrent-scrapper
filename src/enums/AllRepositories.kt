package enums

import repositories.*

enum class AllRepositories(val server: BaseRepository) {
    YGGTORRENT(YggRepository),
   // LEETX(LeetXRepository),
    // THEPIRATEBAY(ThePirateBayRepository),
    // RARBG(RarbgRepository),
    EZTV(EztvRepository),
    TORRENT9(Torrent9Repository)
}