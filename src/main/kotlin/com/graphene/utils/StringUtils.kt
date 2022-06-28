package com.graphene.utils

fun isBlank(value: String) : Boolean {
    if (value.isEmpty()) {
        return true
    }

    return value.toCharArray().all { it.isWhitespace() }
}