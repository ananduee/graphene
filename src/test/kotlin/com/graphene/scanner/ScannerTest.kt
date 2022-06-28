package com.graphene.scanner

import org.junit.jupiter.api.Test

/**
 * We need to make all tests pass from graphql-js
 *
 * https://github.com/graphql/graphql-js/blob/59c87c39c277d6337981f63302bee37dfc3dcebc/src/language/__tests__/lexer-test.ts
 */
internal class ScannerTest {
    @Test
    fun scanTokens() {
        val scanner = Scanner(
            """
            # this is a comment
            query GetUser(${'$'}userId: ID!) {
              user() {
                id,
                name,
                isViewerFriend,
                profilePicture(size: 50)  {
                  ...PictureFragment
                }
              }
            }
                # this is a comment
            fragment PictureFragment on Picture {
              uri,
              width,
              height
            }

        """.trimIndent()
        )
        println(scanner.scanTokens())
    }
}