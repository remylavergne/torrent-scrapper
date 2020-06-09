package Exts

import java.text.Normalizer
import java.text.SimpleDateFormat

val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

fun String.rarbgTimestamp(): Long {
    val test = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-10-11 10:17:55")
    return test.time
}