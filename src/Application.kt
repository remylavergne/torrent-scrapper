import dev.remylavergne.services.OkHttpHelper
import enums.AllRepositories
import helpers.MoshiHelper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.basic
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import models.Torrent

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.modulee() {
    installPlugins(this)
    OkHttpHelper.init(this)

    routing {
        root()
    }
}

@KtorExperimentalAPI
fun installPlugins(application: Application) {
    application.install(CORS) {
        anyHost()
    }
    application.install(StatusPages) {
        exception { cause: Throwable ->
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    application.install(CallLogging)
    application.install(ContentNegotiation) {
        gson {
            // Configure Gson here
        }
    }
    application.install(DefaultHeaders)
    application.install(Authentication) {
        basic(name = "admin") {
            realm = "Api Stats"
            validate { credentials ->
                if (credentials.name == application.environment.config.property("admin-authentication.username")
                        .getString() && credentials.password == application.environment.config.property("admin-authentication.username")
                        .getString()
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}

fun Routing.root() {
    get("/{query}") {
        // TODO => Mettre dans un Service !
        val query = this.call.parameters["query"]
        var response = "{ \"error\": \"No Data\" }"

        if (query != null) {
            val results = mutableListOf<Torrent>()
            val deferred = mutableListOf<Deferred<List<Torrent>>>()

            AllRepositories.values().forEach { repository ->
                deferred.add(async { repository.server.search(query) })
            }

            deferred.forEach { d ->
                results.addAll(d.await())
            }

            val finalData = results.sortedByDescending { it.seeders }

            val adapter = MoshiHelper.getCustomTorrentListAdapter(Torrent::class.java)
            response = adapter.toJson(finalData)
        }

        this.call.respond(response)
    }
}

