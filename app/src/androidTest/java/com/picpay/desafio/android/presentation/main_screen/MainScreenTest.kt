package com.picpay.desafio.android.presentation.main_screen

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.core.app.ApplicationProvider
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.FakeUserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    private lateinit var repository: FakeUserRepository
    private lateinit var viewModel: MainViewModel


    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        repository = FakeUserRepository()
        viewModel = MainViewModel(repository)
        composeTestRule.setContent {
            MainScreen(viewModel)
        }
    }

    @Test
    fun whenScreenIsLoadedShouldDisplayTheTitle() {
        composeTestRule
            .onNodeWithText(context.getString(R.string.title))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenMainScreenFetchUsersIsLoadingShouldDisplayProgressIndicator() {
        composeTestRule
            .onNodeWithTag(context.getString(R.string.progress_indicator))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenMainScreenGetUsersSuccessfullyShouldDisplayUsers() {
        composeTestRule.waitUntil {
            viewModel.state.value.isLoading == false
        }

        composeTestRule
            .onNodeWithTag(context.getString(R.string.user_list))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("test-name")
            .assertExists()

        composeTestRule
            .onNodeWithText("test-username")
            .assertExists()
    }

    @Test
    fun whenMainScreenFetchUsersWithErrorShouldDisplayTheErrorMessage() {
        repository.setGetUsersError()

        composeTestRule.waitUntil {
            viewModel.state.value.isLoading == false
        }

        composeTestRule
            .onNodeWithText("get-users-test-error")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenSyncUsersShouldDisplayPullToRefresh() {
        composeTestRule
            .onRoot()
            .performTouchInput {
                swipeDown()
            }

        composeTestRule
            .onNodeWithTag(context.getString(R.string.pull_to_refresh))
            .assertExists()
            .assertIsDisplayed()
    }
}