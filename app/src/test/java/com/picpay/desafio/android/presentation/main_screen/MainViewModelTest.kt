@file:OptIn(ExperimentalCoroutinesApi::class)

package com.picpay.desafio.android.presentation.main_screen

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
        assertEquals(MainState.Loading, initialState)
    }

    @Test
    fun whenGetUsersSuccessfullyShouldReturnSuccessState() = runTest {
        val user = User(img = "img", name = "test", id = 1, username = "test")
        val users: List<User> = listOf(user, user)
        coEvery { repository.getUsers() } returns users

        mainViewModel.fetchUsers()
        runCurrent()

        coVerify { repository.getUsers() }

        val state = mainViewModel.state.first()
        assert(state is MainState.Success)

        val result = state as MainState.Success
        assertEquals(users.size, result.users.size)
        assertEquals(users, result.users)
    }

    @Test
    fun whenGetUsersGetErrorShouldReturnErrorStateWithMessage() = runTest {
        val errorMessage = "mocked error"
        coEvery { repository.getUsers() } throws Exception(errorMessage)

        mainViewModel.fetchUsers()
        runCurrent()

        coVerify { repository.getUsers() }

        val state = mainViewModel.state.first()
        assert(state is MainState.Error)

        val result = state as MainState.Error
        assertEquals(result.message, errorMessage)
    }
}