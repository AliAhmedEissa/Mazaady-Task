package com.mazaady.android_task.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.model.CustomError
import com.mazaady.android_task.domain.model.OptionDomainModel
import com.mazaady.android_task.domain.model.PropertyDomainModel
import com.mazaady.android_task.domain.model.SubCategoryDomainModel
import com.mazaady.android_task.domain.usecase.FetchCategoriesUseCase
import com.mazaady.android_task.domain.usecase.FetchOptionsUseCase
import com.mazaady.android_task.domain.usecase.FetchPropertiesUseCase
import com.mazaady.android_task.domain.usecase.FilterSubCategoriesByCategoryIdUseCase
import com.mazaady.android_task.presentation.model.PropertyModel
import com.mazaady.android_task.util.DataState
import com.mazaady.android_task.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CategoryViewModel
    private lateinit var fetchCategoriesUseCase: FetchCategoriesUseCase
    private lateinit var filterSubCategoriesUseCase: FilterSubCategoriesByCategoryIdUseCase
    private lateinit var fetchPropertiesUseCase: FetchPropertiesUseCase
    private lateinit var fetchOptionsUseCase: FetchOptionsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fetchCategoriesUseCase = mockk()
        filterSubCategoriesUseCase = mockk()
        fetchPropertiesUseCase = mockk()
        fetchOptionsUseCase = mockk()

        viewModel = CategoryViewModel(
            fetchCategoriesUseCase,
            filterSubCategoriesUseCase,
            fetchPropertiesUseCase,
            fetchOptionsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAllCategories success returns transformed categories`() {
        // Given
        val categoryDomain = CategoryDomainModel(
            id = 1,
            name = "Test Category",
            slug = "test",
            children = emptyList()
        )
        val expectedPropertyModel = PropertyModel(id = 1, name = "Test Category")

        coEvery { fetchCategoriesUseCase() } returns flowOf(DataState.Success(listOf(categoryDomain)))

        // When
        viewModel.fetchAllCategories()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.allCategories.getOrAwaitValue()
        assertTrue(result is DataState.Success)
        assertEquals(expectedPropertyModel, (result as DataState.Success).data?.first())
    }

    @Test
    fun `filterSubCategoriesByCategoryId success returns transformed subcategories with other option`() {
        // Given
        val subCategoryDomain = SubCategoryDomainModel(
            id = 2,
            name = "Test SubCategory",
            slug = "test",
           1
        )

        coEvery { filterSubCategoriesUseCase(1) } returns flowOf(DataState.Success(listOf(subCategoryDomain)))

        // When
        viewModel.filterSubCategoriesByCategoryId(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.subCategories.getOrAwaitValue()
        assertTrue(result is DataState.Success)
        val data = (result as DataState.Success).data
        assertEquals(2, data?.size) // Including "other" option
        assertEquals("other", data?.first()?.name)
        assertEquals(2, data?.get(1)?.id)
    }

    @Test
    fun `fetchPropertiesBySubCategoryId success returns transformed properties`() {
        // Given
        val optionDomain = OptionDomainModel(
            id = 3,
            name = "Test Option",
            slug = "test",
            hasChild = true,
            subCategoryId = 1
        )
        val propertyDomain = PropertyDomainModel(
            id = 2,
            name = "Test Property",
            slug = "test",
            options = listOf(optionDomain)
        )

        coEvery { fetchPropertiesUseCase(1) } returns flowOf(DataState.Success(listOf(propertyDomain)))

        // When
        viewModel.fetchPropertiesBySubCategoryId(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.properties.getOrAwaitValue()
        assertTrue(result is DataState.Success)
        val data = (result as DataState.Success).data
        assertEquals(1, data?.size)
        assertEquals(2, data?.first()?.id)
        assertEquals(1, data?.first()?.options?.size)
        assertTrue(data?.first()?.options?.first()?.hasChild == true)
    }


    @Test
    fun `fetchAllCategories error returns error state`() {
        // Given
        val error = DataState.Error(CustomError("Network Error"))
        coEvery { fetchCategoriesUseCase() } returns flowOf(error)

        // When
        viewModel.fetchAllCategories()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.allCategories.getOrAwaitValue()
        assertTrue(result is DataState.Error)
        assertEquals("Network Error", (result as DataState.Error).error.message)
    }

    @Test
    fun `fetchAllCategories with null data returns empty list`() {
        // Given
        coEvery { fetchCategoriesUseCase() } returns flowOf(DataState.Success(null))

        // When
        viewModel.fetchAllCategories()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.allCategories.getOrAwaitValue()
        assertTrue(result is DataState.Success)
        assertEquals(emptyList<PropertyModel>(), (result as DataState.Success).data)
    }

    @Test
    fun `filterSubCategoriesByCategoryId with null data returns only other option`() {
        // Given
        coEvery { filterSubCategoriesUseCase(1) } returns flowOf(DataState.Success(null))

        // When
        viewModel.filterSubCategoriesByCategoryId(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.subCategories.getOrAwaitValue()
        assertTrue(result is DataState.Success)
        val data = (result as DataState.Success).data
        assertEquals(1, data?.size)
        assertEquals("other", data?.first()?.name)
        assertEquals(-1, data?.first()?.id)
    }

    @Test
    fun `fetchPropertiesBySubCategoryId with server error returns server error state`() {
        // Given
        coEvery { fetchPropertiesUseCase(1) } returns flowOf(DataState.ServerError)

        // When
        viewModel.fetchPropertiesBySubCategoryId(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val result = viewModel.properties.getOrAwaitValue()
        assertTrue(result is DataState.ServerError)
    }
}