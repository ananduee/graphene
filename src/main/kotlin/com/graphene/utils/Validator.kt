package com.graphene.utils

object Validator {

    fun isNotBlank(value: String, messageSupplier: () -> String) {
        if (StringUtils.isBlank(value)) {
            throw IllegalArgumentException(messageSupplier())
        }
    }
}