package com.graphene.scanner

import com.graphene.scanner.model.Token
import com.graphene.scanner.model.TokenType
import com.graphene.utils.validateIsNotBlank

class Scanner(private val source: String) {

    private var tokens: MutableList<Token>
    private var startIdx: Int
    private var currentIdx: Int
    private var currentLine: Int

    init {
        validateIsNotBlank(source) { "Empty source supplied." }
        tokens = ArrayList()
        startIdx = 0
        currentIdx = 0
        currentLine = 1
    }

    fun scanTokens() : List<Token> {
        while (!isAtEnd()) {
            startIdx = currentIdx
            scanToken()
        }
        return tokens
    }

    private fun scanToken() {
        var c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            ':' -> addToken(TokenType.COLON)
            '!' -> addToken(TokenType.NEGATE)
            '.' -> expandOperator()
            '#' -> while (peek() != '\n' && !isAtEnd()) advance() // comment block
            '\n' -> currentLine++
            ' ' -> {}
            else -> when {
                isNumeric(c) -> number()
                isAlpha(c) -> identifier()
                else -> throw IllegalArgumentException("Found invalid token $c on line number $currentLine idx $startIdx")
            }
        }
    }

    private fun advance(): Char {
        currentIdx++
        return source[currentIdx - 1]
    }

    private fun peek(): Char {
        if (isAtEnd()) {
            return '\t'
        }
        return source[currentIdx]
    }

    private fun peekNext(next: Int = 1): Char {
        if (currentIdx + next >= source.length) return '\t'
        return source[currentIdx+next]
    }

    private fun number() {
        while (isNumeric(peek())) advance()
        if (peek() == '.' && isNumeric(peekNext())) {
            advance() // Consume the "."
            while (isNumeric(peek())) advance()
        }
        addToken(TokenType.NUMBER)
    }

    private fun identifier() {
        while (isAlpha(peek())) advance()
        when (source.substring(startIdx, currentIdx)) {
            "query" -> addToken(TokenType.QUERY)
            "mutation" -> addToken(TokenType.MUTATION)
            "type" -> addToken(TokenType.TYPE)
            else -> addToken(TokenType.IDENTIFIER)
        }
    }

    private fun expandOperator() {
        while (peek() == '.') advance()
        addToken(TokenType.EXPAND)
    }

    private fun addToken(tokenType: TokenType) {
        tokens.add(Token(tokenType, source.substring(startIdx, currentIdx), currentLine))
    }

    private fun isAlpha(c: Char): Boolean = c.isLetter() || c == '$'

    private fun isNumeric(c: Char): Boolean = c.isDigit()

    private fun isAtEnd(): Boolean = currentIdx >= source.length
}