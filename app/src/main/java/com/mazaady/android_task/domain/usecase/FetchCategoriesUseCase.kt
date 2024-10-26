package com.mazaady.android_task.domain.usecase

import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.repository.CategoryRepository
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke():Flow<DataState<List<CategoryDomainModel>?>> {
        return repository.fetchCategories()
    }
}
