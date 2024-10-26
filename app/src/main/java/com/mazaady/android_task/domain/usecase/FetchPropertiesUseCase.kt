package com.mazaady.android_task.domain.usecase

import com.mazaady.android_task.domain.model.PropertyDomainModel
import com.mazaady.android_task.domain.repository.CategoryRepository
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPropertiesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(subCategoryId: Int): Flow<DataState<List<PropertyDomainModel>?>> {
        return repository.fetchPropertiesBySubCategoryId(subCategoryId)
    }
}
