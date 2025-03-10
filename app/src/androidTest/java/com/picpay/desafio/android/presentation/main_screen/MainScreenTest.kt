package com.picpay.desafio.android.presentation.main_screen

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.picpay.desafio.android.presentation.utils.FakeUserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    private lateinit var repository: FakeUserRepository

    @Before
    fun setUp() {
        repository = FakeUserRepository()
    }

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun whenMainScreenGetUsersSuccessfully() {
        composeTestRule.setContent {
            val viewModel = MainViewModel(repository)
            MainScreen(viewModel)
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("test-name")
            .assertExists()
    }

    @Test
    fun whenMainScreenGetUsersWithError() {
        repository.setError()

        composeTestRule.setContent {
            val viewModel = MainViewModel(repository)
            MainScreen(viewModel)
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("test-error")
            .assertExists()
            .assertIsDisplayed()
    }
}