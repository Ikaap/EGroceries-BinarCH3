package com.catnip.egroceries.presentation.feature.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.egroceries.data.repository.CartRepository
import com.catnip.egroceries.tools.MainCoroutineRule
import com.catnip.egroceries.tools.getOrAwaitValue
import com.catnip.egroceries.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CartViewModelTest {

    // 1. liat dependensis di viewmodelnya dulu
    @MockK
    private lateinit var repo: CartRepository

    private lateinit var viewModel: CartViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule : TestRule = MainCoroutineRule(UnconfinedTestDispatcher())


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        // mocking resultnya, langusng di success saja
        coEvery { repo.getUserCardData() } returns flow {
            emit(
                ResultWrapper.Success(
                    Pair(
                        listOf(
                            mockk(relaxed = true),
                            mockk(relaxed = true)
                        ),
                        127000.0
                    )
                )
            )
        }
        viewModel = spyk(CartViewModel(repo)) // karena di viewmodelnya tidak ada fun private
        // bikin variabel untuk returnnya karena dia returnnya sama, jadi boleh dibuat
        val updateResultMock = flow {
            emit(ResultWrapper.Success(true))
        }
        coEvery { repo.decreaseCart(any()) } returns updateResultMock
        coEvery { repo.increaseCart(any()) } returns updateResultMock
        coEvery { repo.deleteCart(any()) } returns updateResultMock
        coEvery { repo.setCartNotes(any()) } returns updateResultMock

    }

    @Test
    fun `test cart list`(){
        val result = viewModel.cartList.getOrAwaitValue()
        assertEquals(result.payload?.first?.size, 2)
        assertEquals(result.payload?.second, 127000.0)
    }

    @Test
    fun `test decrease cart`(){
      viewModel.decreaseCart(mockk())
        coVerify { repo.decreaseCart(any()) }
    }

    @Test
    fun `test increase cart`(){
        viewModel.increaseCart(mockk())
        coVerify { repo.increaseCart(any()) }
    }

    @Test
    fun `test remove cart`(){
        viewModel.removeCart(mockk())
        coVerify { repo.deleteCart(any()) }
    }

    @Test
    fun `test set cart notes`(){
        viewModel.setCartNotes(mockk())
        coVerify { repo.setCartNotes(any()) }
    }


}
