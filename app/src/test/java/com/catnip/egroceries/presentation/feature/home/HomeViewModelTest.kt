package com.catnip.egroceries.presentation.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.egroceries.data.repository.ProductRepository
import com.catnip.egroceries.model.Category
import com.catnip.egroceries.model.Product
import com.catnip.egroceries.presentation.feature.home.adapter.model.HomeSection
import com.catnip.egroceries.tools.MainCoroutineRule
import com.catnip.egroceries.tools.getOrAwaitValue
import com.catnip.egroceries.utils.AssetWrapper
import com.catnip.egroceries.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @MockK
    private lateinit var repo: ProductRepository

    @MockK
    private lateinit var assetWrapper: AssetWrapper

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule : TestRule = MainCoroutineRule(UnconfinedTestDispatcher())


    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            HomeViewModel(repo, assetWrapper),
            recordPrivateCalls = true
        )

        // mocking result
        every { assetWrapper.getString(any()) } returns "any"
        coEvery { repo.getCategories() } returns flow {
            emit(ResultWrapper.Success(listOf(
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true)
            )))
        }
        coEvery { repo.getProducts(any()) } returns flow {
            emit(ResultWrapper.Success(listOf(
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true),
                mockk(relaxed = true)
            )))
        }
    }

    @Test
    fun `home data tes`(){
        val result = viewModel.homeData.getOrAwaitValue()
        assertEquals(result.size, 4)
        assertTrue(result[0] is HomeSection.HeaderSection)
        assertTrue(result[1] is HomeSection.BannerSection)
        assertTrue(result[2] is HomeSection.CategoriesSection)
        assertTrue(result[3] is HomeSection.ProductsSection)
        assertEquals((result[2] as HomeSection.CategoriesSection).data.payload?.size, 3)
        assertEquals((result[3] as HomeSection.ProductsSection).data.payload?.size, 4)
        // memastikan apakah private fun maptohomedata dipanggil atau tidak
        verify {
            // karena dia private maka harus menggunakan invoke, dengan nama functionnya sama dengan yang ada di viewmodel
            viewModel invoke "mapToHomeData" withArguments listOf(
                any<ResultWrapper<List<Category>>>(),
                any<ResultWrapper<List<Product>>>() // argument harus spesify
            )

        }
        verify { viewModel.setSelectedCategory() } // verivy digunakan untuk memastikan method didalamnya dipanggil atau tidak

    }
}