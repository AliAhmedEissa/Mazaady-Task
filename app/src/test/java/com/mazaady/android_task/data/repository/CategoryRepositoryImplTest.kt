package com.mazaady.android_task.data.repository

import com.mazaady.android_task.data.api.ApiService
import com.mazaady.android_task.data.model.Category
import com.mazaady.android_task.data.model.CategoryData
import com.mazaady.android_task.data.model.CategoryResponse
import com.mazaady.android_task.data.model.OptionsItem
import com.mazaady.android_task.data.model.PropertiesResponse
import com.mazaady.android_task.data.model.Property
import com.mazaady.android_task.data.model.SubCategory
import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.model.SubCategoryDomainModel
import com.mazaady.android_task.util.DataState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CategoryRepositoryImplTest {
    private lateinit var repository: CategoryRepositoryImpl
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mockk()
        repository = CategoryRepositoryImpl(apiService)
    }

    @Test
    fun `fetchCategories success returns cached categories`() = runBlocking {
        // Given
        val category = Category(
            id = 1,
            name = "Test Category",
            description = "Test Description",
            image = "test.jpg",
            slug = "test",
            children = listOf(
                SubCategory(
                    id = 2,
                    name = "Test SubCategory",
                    description = "Test Sub Description",
                    image = "test_sub.jpg",
                    slug = "test_sub",
                    children = null,
                    circle_icon = null,
                    disable_shipping = null
                )
            ),
            circle_icon = "icon.png",
            disable_shipping = 0
        )

        val categoryResponse = CategoryResponse(
            code = 200,
            msg = "Success",
            data = CategoryData(
                categories = listOf(category),
                ios_version = "1.0",
                ios_latest_version = "1.1",
                google_version = "1.0",
                huawei_version = "1.0"
            )
        )

        coEvery { apiService.fetchAllCategories() } returns Response.success(categoryResponse)

        // When
        val results = repository.fetchCategories().toList()

        // Then
        assertTrue(results[0] is DataState.Processing)
        assertTrue(results[1] is DataState.Idle)  // Added Idle state check
        assertTrue(results[2] is DataState.Success)  // Changed index to 2
        val successState = results[2] as DataState.Success  // Changed index to 2
        assertEquals(1, successState.data?.size)
        assertEquals(category.id, successState.data?.first()?.id)
    }

    @Test
    fun `filterSubCategoriesByCategoryIds returns correct subcategories`() = runBlocking {
        // Given
        val categoryId = 1
        val subCategory = SubCategoryDomainModel(
            id = 2,
            name = "Test SubCategory",
            slug = "test",
           1
        )

        repository.cachedCategories = listOf(
            CategoryDomainModel(
                id = categoryId,
                name = "Test Category",
                slug = "test",
                children = listOf(subCategory)
            )
        )

        // When
        val results = repository.filterSubCategoriesByCategoryIds(categoryId).toList()

        // Then
        assertTrue(results[0] is DataState.Success)
        val successState = results[0] as DataState.Success
        assertEquals(1, successState.data?.size)
        assertEquals(subCategory.id, successState.data?.first()?.id)
    }

    @Test
    fun `fetchPropertiesBySubCategoryId success returns properties`() = runBlocking {
        // Given
        val subCategoryId = 1
        val property = Property(
            id = 1,
            name = "Test Property",
            description = "Test Description",
            options = listOf(
                OptionsItem(
                    id = 1,
                    name = "Option 1",
                    slug = "option-1",
                    parent = null,
                    child = false
                )
            ),
            list = false,
            parent = null,
            slug = "test",
            type = "text",
            value = "test",
            otherValue = null
        )

        val propertiesResponse = PropertiesResponse(
            code = 200,
            msg = "Success",
            data = listOf(property)
        )

        coEvery { apiService.fetchPropertiesBySubCategoryId(subCategoryId) } returns Response.success(propertiesResponse)

        // When
        val results = repository.fetchPropertiesBySubCategoryId(subCategoryId).toList()

        // Then
        assertTrue(results[0] is DataState.Processing)
        assertTrue(results[1] is DataState.Idle)  // Added Idle state check
        assertTrue(results[2] is DataState.Success)  // Changed index to 2
        val successState = results[2] as DataState.Success  // Changed index to 2
        assertEquals(1, successState.data?.size)
        assertEquals(property.id, successState.data?.first()?.id)
    }

}