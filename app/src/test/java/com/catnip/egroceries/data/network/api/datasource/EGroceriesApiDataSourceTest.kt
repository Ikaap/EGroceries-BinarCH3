package com.catnip.egroceries.data.network.api.datasource

import com.catnip.egroceries.data.network.api.model.category.CategoriesResponse
import com.catnip.egroceries.data.network.api.model.order.OrderRequest
import com.catnip.egroceries.data.network.api.model.order.OrderResponse
import com.catnip.egroceries.data.network.api.model.product.ProductsResponse
import com.catnip.egroceries.data.network.api.service.EGroceriesApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EGroceriesApiDataSourceTest {

    // mocking adalah memalsukan result dari sebuah sumber data, agar resultnya sesuai dengan apa yang kita inginkan
    @MockK
    // service dijadikan mock karena dia trut of data source (sumber data bagi datasource)
    lateinit var service: EGroceriesApiService

    // data source tidak di mock karena dia yang mau di testing
    private lateinit var dataSource: EGroceriesDataSource

    @Before
    fun setUp() {
        // setelah menentukan apa yang dimock wajib me mockKAnnotations kan agar terpanggil
        MockKAnnotations.init(this)
        dataSource = EGroceriesApiDataSource(service)
    }

    @Test
    fun getProducts() {
        runTest {
            val mockResponse = mockk<ProductsResponse>()
            coEvery { service.getProducts(any()) } returns mockResponse
            val response = dataSource.getProducts("makanan")
            coVerify { service.getProducts(any()) } // memverivikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() } // memverivikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) } // memverivikasi apakah service sudah terpanggil
            assertEquals(response, mockResponse) // mencocokan hasil antara actual dengan expected
        }
    }
}
