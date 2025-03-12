@file:OptIn(ExperimentalCoroutinesApi::class)

package com.picpay.desafio.android.presentation.main_screen

import app.cash.turbine.test
import com.picpay.desafio.android.data.FakeUserRepository
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.util.TestDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class MainViewModelTest : KoinTest {

    private val repository = FakeUserRepository()
    private val testModule = module {
        viewModel { MainViewModel(repository) }
    }

    private val viewModel: MainViewModel by inject()

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    @Before
    fun setup() {
        startKoin { modules(testModule) }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun whenFetchUsersIsLoadingShouldReturnIsLoading() = runTest {
        val state = viewModel.state.first()
        assertEquals(MainState(isLoading = true), state)
    }

    @Test
    fun whenFetchUsersGetSuccessShouldReturnTheUsers() = runTest {
        val expectedUsers = listOf(
            User(
                id = 1,
                name = "test-name",
                username = "test-username",
                img = "test-img"
            )
        )
        viewModel.state.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(expectedUsers, state.users)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun whenFetchUsersGetErrorShouldReturnErrorMessage() = runTest {
        val expectedErrorMessage = "get-users-test-error"
        repository.setGetUsersError()

        viewModel.state.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(expectedErrorMessage, state.error)
        }
    }

    @Test
    fun whenSyncUsersIsLoadingShouldReturnIsSyncing() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.syncUsers()
            val state = awaitItem()

            awaitItem()

            assertEquals(MainState(isSyncing = true), state)
        }
    }

    @Test
    fun whenSyncUsersGetSuccessShouldReturnNothing() = runTest {
        viewModel.syncUsers()
        viewModel.state.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(false, state.isSyncing)
            assertEquals(null, state.syncError)
        }
    }

    @Test
    fun whenSyncUsersGetErrorShouldReturnErrorMessage() = runTest {
        val expectedErrorMessage = "sync-users-test-error"
        repository.setSyncUsersError()

        viewModel.state.test {
            awaitItem()

            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(false, state.isSyncing)
            assertEquals(expectedErrorMessage, state.syncError)
        }
    }
}