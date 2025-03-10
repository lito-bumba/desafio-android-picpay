@file:OptIn(ExperimentalCoroutinesApi::class)

package com.picpay.desafio.android.presentation.main_screen

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class MainViewModelTest : KoinTest {

    private val testModule = module {
        single<UserRepository> { mockk() }
        viewModel { MainViewModel(get()) }
    }

    private val repository: UserRepository by inject()
    private val mainViewModel: MainViewModel by inject()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoin { modules(testModule) }
    }

    @After
    fun teardown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun whenGetInitialStateShouldReturnLoadingState() = runTest {
        val initialState = mainViewModel.state.first()
        assertEquals(MainState(isLoading = true), initialState)
    }

    @Test
    fun whenGetUsersSuccessfullyShouldReturnListOfUsers() = runTest {
        val usersFlow = flowOf(emptyList<User>())
        coEvery { repository.getUsers() } returns Result.success(usersFlow)

        coEvery { repository.syncUsers() } just Awaits

        val collectedUsers = mutableListOf<List<User>>()
        val job = launch {
            mainViewModel.state.map { it.users }.toList(collectedUsers)
        }


        mainViewModel.fetchUsers()
        runCurrent()

        coVerify { repository.getUsers() }
        coVerify { repository.syncUsers() }

        assertEquals(collectedUsers.last(), emptyList<User>())
        assertEquals(collectedUsers.last().size, 0)

        job.cancel()
    }

    @Test
    fun whenGetUsersGetErrorShouldReturnErrorMessage() = runTest {
        val errorMessage = "mocked error"
        coEvery { repository.getUsers() } returns Result.failure(Exception(errorMessage))
        coEvery { repository.syncUsers() } just Awaits

        mainViewModel.state.collect { state ->
            assertEquals(errorMessage, state.error)
        }

        mainViewModel.fetchUsers()
        runCurrent()

        coVerify { repository.getUsers() }
        coVerify { repository.syncUsers() }

        val state = mainViewModel.state.first()
        assert(state.isLoading == false)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun whenSyncUsersSuccessfullyShouldReturnSuccessState() = runTest {
        coEvery { repository.syncUsers() } returns Result.success(Unit)

        mainViewModel.syncUsers()
        runCurrent()

        coVerify { repository.syncUsers() }

        val state = mainViewModel.state.first()
        assert(state.isSyncing == false)
        assertEquals(null, state.syncError)
    }

    @Test
    fun whenSyncUsersGetErrorShouldReturnErrorStateWithMessage() = runTest {
        val errorMessage = "mocked error"
        coEvery { repository.getUsers() } throws Exception(errorMessage)

//        mainViewModel.fetchUsers()
        runCurrent()

        coVerify { repository.getUsers() }

        val state = mainViewModel.state.first()
        assert(state.isLoading == false)
        assertEquals(errorMessage, state.error)
    }
}