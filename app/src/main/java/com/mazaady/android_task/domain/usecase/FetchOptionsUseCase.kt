package com.mazaady.android_task.domain.usecase

import com.mazaady.android_task.domain.model.OptionChildDomain
import com.mazaady.android_task.domain.repository.CategoryRepository
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOptionsUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(optionId: Int): Flow<DataState<List<OptionChildDomain>?>>{
        return repository.fetchChildOptionsByOptionId(optionId)
    }
}
