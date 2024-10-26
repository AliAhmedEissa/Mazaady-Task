package com.mazaady.android_task.data.repository

import com.mazaady.android_task.data.api.ApiService
import com.mazaady.android_task.data.common.BaseRepo
import com.mazaady.android_task.data.mapper.toCategoriesDomainModel
import com.mazaady.android_task.data.mapper.toOptionChildDomain
import com.mazaady.android_task.data.mapper.toPropertyDomainModels
import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.model.OptionChildDomain
import com.mazaady.android_task.domain.model.PropertyDomainModel
import com.mazaady.android_task.domain.model.SubCategoryDomainModel
import com.mazaady.android_task.domain.repository.CategoryRepository
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) :BaseRepo(), CategoryRepository {

    var cachedCategories: List<CategoryDomainModel>? = null

    // Fetch all categories
    override suspend fun fetchCategories(): Flow<DataState<List<CategoryDomainModel>?>> {
        return performApiCall(apiCall = { apiService.fetchAllCategories() }).map { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    // Cache the categories after fetching them
                    cachedCategories = dataState.data?.data?.toCategoriesDomainModel()
                    DataState.Success(cachedCategories)
                }
                is DataState.Error -> dataState
                is DataState.Processing -> dataState
                is DataState.ServerError -> dataState
                is DataState.Idle -> dataState
            }
        }
    }

    // Fetch subcategories by category IDs
    override suspend fun filterSubCategoriesByCategoryIds(categoryId: Int): Flow<DataState<List<SubCategoryDomainModel>?>> {
        return flow {
            val subCategories = cachedCategories?.filter { it.id == categoryId }?.flatMap { it.children } ?: emptyList()
            emit(DataState.Success(subCategories))
        }
    }

    // Fetch Properties  by subcategory ID
    override suspend fun fetchPropertiesBySubCategoryId(subCategoryId: Int): Flow<DataState<List<PropertyDomainModel>?>> {
        return performApiCall(apiCall = { apiService.fetchPropertiesBySubCategoryId(subCategoryId) }).map { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    DataState.Success( dataState.data?.data?.toPropertyDomainModels())
                }
                is DataState.Error -> dataState
                is DataState.Processing -> dataState
                is DataState.ServerError -> dataState
                is DataState.Idle -> dataState
            }
        }
    }

    // Fetch Properties  by subcategory ID
    override suspend fun fetchChildOptionsByOptionId(optionId: Int): Flow<DataState<List<OptionChildDomain>?>> {
        return performApiCall(apiCall = { apiService.fetchOptionsChild(optionId) }).map { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    DataState.Success( dataState.data?.data?.map { it.toOptionChildDomain() })
                }
                is DataState.Error -> dataState
                is DataState.Processing -> dataState
                is DataState.ServerError -> dataState
                is DataState.Idle -> dataState
            }
        }
    }

}
