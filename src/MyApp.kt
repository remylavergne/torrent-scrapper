import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Unused at this time
 */

val client = getUnsafeOkHttpClient()
    .newBuilder()
    .followRedirects(false)
    .build()
val urlGeneric = "https://www2.yggtorrent.pe/engine/ajax_top_query/day"


fun maind() {
    var currentUrl: String = ""
    lateinit var currentHeaders: Headers
    var cookiesString: String = ""
    lateinit var currentResponse: Response

    currentResponse = makeRequest(urlGeneric, "")
    // TODO: Récupérer les cookies
    currentResponse.headers.forEach { (key, value) ->
        //println("$key : $value")
        if (key == "set-cookie") {
            cookiesString += "$value; "
        }
    }
    currentUrl = currentResponse.headers["location"]!!
    currentHeaders = currentResponse.headers

    currentResponse = makeRequest(currentResponse.headers["location"]!!.replace("http", "https"), cookiesString)

    val jsonObject = fromJson<JsonObject>(currentResponse.body?.string()!!)

    val allKeys = jsonObject.keySet()

    val get = jsonObject.get(allKeys.first())

    println("Breakpoint")

}

inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object: TypeToken<T>(){}.type)
}

/**
 * Accept all certificates from CLoudFlare or other providers
 */
fun getUnsafeOkHttpClient(): OkHttpClient {
    // Create a trust manager that does not validate certificate chains
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
    })

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier(HostnameVerifier { _, _ -> true }).build()
}


fun makeRequest(url: String, cookie: String): Response {

    val request = Request.Builder().url(url)
        .header(
            "accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
        )
        .header("accept-encoding", "identity")
        .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
        .header("sec-fetch-site", "none")
        .header("upgrade-insecure-requests", "1")
        .header(
            "user-agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36"
        )
        .header("cookie", cookie)
        .header("content-type", "application/json")
        .build()

    return client.newCall(request).execute()
}