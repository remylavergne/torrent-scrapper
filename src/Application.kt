package dev.remylavergne

import dev.remylavergne.services.OkHttpHelper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import okhttp3.Request
import org.jsoup.Jsoup

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    OkHttpHelper.init(this)

 Jsoup.connect("https://www2.yggtorrent.se/engine/search?name=jaw&category=2145&sub_category=all&do=search&attempt=1&order=desc&sort=publish_date").get().run {
     val elements = this.getElementsByClass("table-responsive results")

     println()
 }
    println()


    routing {
        root()
    }
}

fun Routing.root() {
    get("/") {
        call.respondText("Hello, World!")
    }
}

