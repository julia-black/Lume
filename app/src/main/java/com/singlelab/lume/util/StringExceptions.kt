package com.singlelab.lume.util

fun String?.maskPhone(): String {
    if (isNullOrEmpty()) {
        return ""
    } else {
        if (this!!.length == 10) {
            val builder = StringBuilder("+7(")
            this.forEachIndexed { idx, it ->
                builder.append(it)
                if (idx == 2) builder.append(") ")
                if (idx == 5 || idx == 7) builder.append("-")
            }
            return builder.toString()
        }
        return this
    }
}