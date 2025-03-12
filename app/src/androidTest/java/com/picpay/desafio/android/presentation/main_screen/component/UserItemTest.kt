package com.picpay.desafio.android.presentation.main_screen.component

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.model.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var context: Context

    @Before
    fun setUp() {
        composeTestRule.setContent {
            context = LocalContext.current
            UserItem(
                user = User(
                    id = 1,
                    name = "test-name",
                    username = "test-username",
                    img = "test-img"
                )
            )
        }
    }

    @Test
    fun userItemComponentAppearNameText() {
        composeTestRule
            .onNodeWithText("test-name")
            .assertExists()
    }

    @Test
    fun userItemComponentAppearUserNameText() {
        composeTestRule
            .onNodeWithText("test-username")
            .assertExists()
    }

    @Test
    fun userItemComponentAppearUserImage() {
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.profile_picture))
            .assertExists()
            .assertIsDisplayed()
    }
}