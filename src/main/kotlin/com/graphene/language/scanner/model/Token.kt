package com.graphene.language.scanner.model

data class Token(val type: TokenType, val lexMe: String, val lineNumber: Int)
