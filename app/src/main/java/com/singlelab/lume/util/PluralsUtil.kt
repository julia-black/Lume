package com.singlelab.lume.util

object PluralsUtil {

    fun getString(
        value: Int, endsWith1: String, endsWith2: String,
        endsWith3: String, endsWith4: String, other: String
    ): String {
        var mod: Int = value % 100
        if (mod > 19) {
            mod %= 10
        }

        val result: String

        result = when (mod) {
            1 -> endsWith1
            2 -> endsWith2
            3 -> endsWith3
            4 -> endsWith4
            else -> other
        }

        return "$value $result"
    }
}