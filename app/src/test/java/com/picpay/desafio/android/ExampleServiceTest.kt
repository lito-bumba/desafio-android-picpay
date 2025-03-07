package com.picpay.desafio.android

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.dto.UserDto
import junit.framework.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ExampleServiceTest {

    private val api = mock<PicPayService>()

    private val service = ExampleService(api)

    @Test
    fun exampleTest() {
        // given
        val call = mock<Call<List<UserDto>>>()
        val expectedUserDtos = emptyList<UserDto>()

        whenever(call.execute()).thenReturn(Response.success(expectedUserDtos))
        whenever(api.getUsers()).thenReturn(call)

        // when
        val users = service.example()

        // then
        assertEquals(users, expectedUserDtos)
    }
}