package com.mazaady.android_task.domain.repository

import com.mazaady.android_task.data.model.SubCategory
import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.model.OptionChildDomain
import com.mazaady.android_task.domain.model.PropertyDomainModel
import com.mazaady.android_task.domain.model.SubCategoryDomainModel
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun fetchCategories(): Flow<DataState<List<CategoryDomainModel>?>>
    suspend fun filterSubCategoriesByCategoryIds(categoryId: Int): Flow<DataState<List<SubCategoryDomainModel>?>>
    suspend fun fetchPropertiesBySubCategoryId(subCategoryId: Int): Flow<DataState<List<PropertyDomainModel>?>>
    suspend fun fetchChildOptionsByOptionId(optionId: Int): Flow<DataState<List<OptionChildDomain>?>>

}
