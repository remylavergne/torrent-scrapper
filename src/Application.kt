import dev.remylavergne.services.OkHttpHelper
import enums.AllRepositories
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import models.Torrent

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.modulee() {
    OkHttpHelper.init(this)

    routing {
        root()
    }
}

fun Routing.root() {
    get("/") {

        val query = this.call.request.queryParameters["request"]

        val results = mutableListOf<Torrent>()
        val deferred = mutableListOf<Deferred<List<Torrent>>>()

        AllRepositories.values().forEach { repository ->
            deferred.add(async { repository.server.search("rick and morty") })
        }

        deferred.forEach { d ->
            results.addAll(d.await())
        }

        val finalData = results.sortedByDescending { it.seeders }

        call.respondText("${finalData.count()} fichiers trouvés !")
    }
}

