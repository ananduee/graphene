package com.graphene.language.scanner.model

enum class TokenType {

    // Single character tokens
    LEFT_PAREN,RIGHT_PAREN,LEFT_BRACE,RIGHT_BRACE,COMMA,COLON,BANG,PIPE,DOLLAR,AMP,EQUALS,AT,

    // Multiple character tokens
    SPREAD,

    // Literals
    NAME,INT,FLOAT,COMMENT,STRING,BLOCK_STRING,

    // Keywords
    QUERY,MUTATION,TYPE
}