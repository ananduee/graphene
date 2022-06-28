package com.graphene.utils

fun validateIsNotBlank(value: String, messageSupplier: () -> String) {
    if (isBlank(value)) {
        throw IllegalArgumentException(messageSupplier())
    }
}