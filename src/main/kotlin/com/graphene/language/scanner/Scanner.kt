package com.graphene.language.scanner

import com.graphene.language.scanner.model.Token
import com.graphene.language.scanner.model.TokenType
import com.graphene.utils.Validator

class Scanner(private val source: String) {

    private var tokens: MutableList<Token>
    private var startIdx: Int
    private var currentIdx: Int
    private var currentLine: Int

    init {
        Validator.isNotBlank(source) { "Empty source supplied." }
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
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            ':' -> addToken(TokenType.COLON)
            '!' -> addToken(TokenType.BANG)
            '|' -> addToken(TokenType.PIPE)
            '$' -> addToken(TokenType.DOLLAR)
            '&' -> addToken(TokenType.AMP)
            '=' -> addToken(TokenType.EQUALS)
            '@' -> addToken(TokenType.AT)
            '.' -> readExpandOperator()
            '#' -> {
                while (peek() != '\n' && !isAtEnd()) advance()
                addToken(TokenType.COMMENT)
            }
            '"' -> {
                if (peek() == '"' && peekNext(1) == '"') {
                    readBlockString()
                } else {
                    readString()
                }
            }
            '\n' -> currentLine++
            ' ' -> {}
            '\r' -> {} // Move to start of line. We don't keep track of char index so it's okay to not do anything.
            else -> when {
                isNumeric(c) -> readNumber()
                isAlpha(c) -> readIdentifier()
                else -> throw IllegalArgumentException("Found invalid token $c on line number $currentLine idx $startIdx")
            }
        }
    }

    private fun advance(advanceBy: Int = 1): Char {
        currentIdx += advanceBy
        return source[currentIdx - advanceBy]
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

    private fun readString() {
        var lastCharEscape = false
        while (!isAtEnd() && (peek() != '"' || lastCharEscape)) {
            lastCharEscape = peek() == '\\'
            if (advance() == '\n') {
                throw IllegalArgumentException("String is not expected to contain new line in lineNumber $currentLine")
            }
        }
        if (isAtEnd()) {
            throw IllegalArgumentException("Unterminated string found on line $currentLine")
        }
        advance()
        addToken(TokenType.STRING)
    }

    private fun readBlockString() {
        advance(3)
        while ((peek() != '"' && peekNext(1) != '"' && peekNext(2) != '"') && !isAtEnd()) {
            if (advance() == '\n') {
                currentLine++
            }
        }
        addToken(TokenType.BLOCK_STRING)
    }

    private fun readNumber() {
        while (isNumeric(peek())) advance()
        var isInt = true
        if (peek() == '.' && isNumeric(peekNext())) {
            isInt = false
            advance() // Consume the "."
            while (isNumeric(peek())) advance()
        }
        addToken(if (isInt) TokenType.INT else TokenType.FLOAT)
    }

    private fun readIdentifier() {
        while (isAlpha(peek())) advance()
        when (source.substring(startIdx, currentIdx)) {
            "query" -> addToken(TokenType.QUERY)
            "mutation" -> addToken(TokenType.MUTATION)
            "type" -> addToken(TokenType.TYPE)
            else -> addToken(TokenType.NAME)
        }
    }

    private fun readExpandOperator() {
        while (peek() == '.') advance()
        addToken(TokenType.SPREAD)
    }

    private fun addToken(tokenType: TokenType) {
        tokens.add(Token(tokenType, source.substring(startIdx, currentIdx), currentLine))
    }

    private fun isAlpha(c: Char): Boolean = c.isLetter() || c == '$'

    private fun isNumeric(c: Char): Boolean = c.isDigit()

    private fun isAtEnd(): Boolean = currentIdx >= source.length
}