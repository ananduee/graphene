package com.graphene.scanner

import com.graphene.language.scanner.Scanner
import com.graphene.language.scanner.model.Token
import com.graphene.language.scanner.model.TokenType
import org.junit.jupiter.api.Test
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * We need to make all tests pass from graphql-js
 *
 * https://github.com/graphql/graphql-js/blob/59c87c39c277d6337981f63302bee37dfc3dcebc/src/language/__tests__/lexer-test.ts
 */
internal class ScannerTest {

    @Test
    fun testLineBreaks() {
        assertTokenPresent("foo") {
            assertEquals(TokenType.NAME, it.type)
            assertEquals("foo", it.lexMe)
        }
        assertTokenPresent("\nfoo") {
            assertEquals(TokenType.NAME, it.type)
            assertEquals("foo", it.lexMe)
        }
        assertTokenPresent("\rfoo") {
            assertEquals(TokenType.NAME, it.type)
            assertEquals("foo", it.lexMe)
        }
        assertTokenPresent("\r\nfoo") {
            assertEquals(TokenType.NAME, it.type)
            assertEquals("foo", it.lexMe)
            assertEquals(2, it.lineNumber)
        }
        assertTokenPresent("\n\r\n\n\n\nfoo") {
            assertEquals(TokenType.NAME, it.type)
            assertEquals("foo", it.lexMe)
            assertEquals(6, it.lineNumber)
        }
    }

    @Test
    fun testSkipsWhiteSpaceAndComments() {
        val tokens = Scanner("""#comment
    foo#comment""").scanTokens()
        assertEquals(TokenType.NAME, tokens[1].type)
        assertEquals("foo", tokens[1].lexMe)
        assertEquals(2, tokens[1].lineNumber)
    }

    @Test
    fun testHandlesString() {
        assertTokenPresent("\"\"") {
            assertEquals(TokenType.STRING, it.type)
            assertEquals("\"\"", it.lexMe)
        }
        assertTokenPresent("\"simple\"") {
            assertEquals(TokenType.STRING, it.type)
            assertEquals("\"simple\"", it.lexMe)
        }
    }

    private fun assertTokenPresent(source: String, tokenMatcher: Consumer<Token>) {
        val tokens = Scanner(source).scanTokens()
        tokenMatcher.accept(tokens[0])
    }

}