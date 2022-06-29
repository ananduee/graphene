package com.graphene.utils

object StringUtils {

    fun isBlank(value: String) : Boolean {
        if (value.isEmpty()) {
            return true
        }

        return value.all { it.isWhitespace() }
    }
}

