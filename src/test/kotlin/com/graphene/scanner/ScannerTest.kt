package com.graphene.scanner

import com.graphene.language.scanner.Scanner
import com.graphene.language.scanner.model.TokenType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

internal class ScannerTest : DescribeSpec({

    describe("Tracks line breaks") {

        listOf(
            Triple("plain string", "foo", 1),
            Triple("string with line break", "\nfoo", 2),
            Triple("string with \\r", "\rfoo", 2),
            Triple("more complicated string", "\n\n\r\rfoo", 5)
        ).forEach {
            it("test for ${it.first}") {
                val tokens = Scanner(it.second).scanTokens()
                tokens.size shouldBeExactly 1
                tokens[0].type shouldBe TokenType.NAME
                tokens[0].lexMe shouldBe "foo"
                tokens[0].lineNumber shouldBe it.third
            }
        }
    }

    describe("Test comment") {
        it("Validate comment") {
            Scanner("#foo").scanTokens().let {
                it.size shouldBe 1
                it[0].type shouldBe TokenType.COMMENT
                it[0].lexMe shouldBe "foo"
            }
        }

        it("Multiple lines of comments") {
            Scanner("""#foo
                       #foo""".trimMargin()).scanTokens().let {
                it.size shouldBe 2
                it.forEach { token ->
                    token.type shouldBe TokenType.COMMENT
                }
            }
        }
    }

    describe("Handles string values") {
        it("empty string") {
            Scanner("\"\"").scanTokens().let {
                it.size shouldBe 1
                it.first().let {token ->
                    token.type shouldBe TokenType.STRING
                    token.lexMe shouldBe ""
                }
            }
        }

        it("string with value") {
            Scanner("\"foo\"").scanTokens().let {
                it.size shouldBe 1
                it.first().let {token ->
                    token.type shouldBe TokenType.STRING
                    token.lexMe shouldBe "foo"
                }
            }
        }

        it("simple escaped string") {
            Scanner("\"\\\"\"").scanTokens().let {
                it.size shouldBe 1
                it.first().let {token ->
                    token.type shouldBe TokenType.STRING
                    token.lexMe shouldBe "\\\""
                }
            }
        }
    }

})