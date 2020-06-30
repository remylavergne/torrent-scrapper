package Exts

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDate(): String {

    var stringValue = this.toString()

    while (stringValue.count() < 13) {
        stringValue += '0'
    }

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val date = Date(stringValue.toLong())

    return simpleDateFormat.format(date.time)
}