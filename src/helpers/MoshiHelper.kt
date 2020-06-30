package helpers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import models.*


object MoshiHelper {

    fun <T> getAdapter(jsonClass: Class<T>): JsonAdapter<T> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(jsonClass)
    }

    fun <T> getListAdapter(jsonClass: Class<T>): JsonAdapter<List<T>> {
        val type =
            Types.newParameterizedType(List::class.java, jsonClass)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(type)
    }

    inline fun <reified T> getCustomTorrentListAdapter(jsonClass: Class<T>): JsonAdapter<List<T>> {
        val type =
            Types.newParameterizedType(List::class.java, jsonClass)
        val moshi = Moshi.Builder().add(
            PolymorphicJsonAdapterFactory.of(Torrent::class.java, "torrent")
                .withSubtype(YggTorrent::class.java, "ygg")
                .withSubtype(Eztv::class.java, "eztv")
                .withSubtype(Torrent9::class.java, "torrent9")
                .withSubtype(Rarbg::class.java, "rarbg")
                .withSubtype(LeetX::class.java, "leetx")
        )
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi.adapter(type)
    }

}