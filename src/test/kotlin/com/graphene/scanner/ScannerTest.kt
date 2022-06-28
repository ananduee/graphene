package com.graphene.scanner

import org.junit.jupiter.api.Test

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