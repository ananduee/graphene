package com.graphene.scanner.model

enum class TokenType {

    // Single character tokens
    LEFT_PAREN,RIGHT_PAREN,LEFT_BRACE,RIGHT_BRACE,COMMA,COLON,NEGATE,

    // Multiple character tokens
    EXPAND,

    // Literals
    IDENTIFIER,NUMBER,

    // Keywords
    QUERY,MUTATION,TYPE
}