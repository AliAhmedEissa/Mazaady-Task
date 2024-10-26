package com.mazaady.android_task.domain.usecase

import com.mazaady.android_task.domain.model.SubCategoryDomainModel
import com.mazaady.android_task.domain.repository.CategoryRepository
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterSubCategoriesByCategoryIdUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int): Flow<DataState<List<SubCategoryDomainModel>?>> {
        return repository.filterSubCategoriesByCategoryIds(categoryId)
    }
}
