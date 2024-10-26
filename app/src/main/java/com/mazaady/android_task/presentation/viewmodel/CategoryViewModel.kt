package com.mazaady.android_task.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazaady.android_task.domain.usecase.FetchCategoriesUseCase
import com.mazaady.android_task.domain.usecase.FetchOptionsUseCase
import com.mazaady.android_task.domain.usecase.FetchPropertiesUseCase
import com.mazaady.android_task.domain.usecase.FilterSubCategoriesByCategoryIdUseCase
import com.mazaady.android_task.presentation.model.PropertyModel
import com.mazaady.android_task.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val filterSubCategoriesByCategoryIdUseCase: FilterSubCategoriesByCategoryIdUseCase,
    private val fetchPropertiesUseCase: FetchPropertiesUseCase,
    private val fetchOptionsUseCase: FetchOptionsUseCase,
) : ViewModel() {

    private val _allCategories = MutableLiveData<DataState<List<PropertyModel>>>()
    val allCategories: LiveData<DataState<List<PropertyModel>>> get() = _allCategories

    private val _subCategories = MutableLiveData<DataState<List<PropertyModel>>>()
    val subCategories: LiveData<DataState<List<PropertyModel>>> get() = _subCategories

    private val _properties = MutableLiveData<DataState<List<PropertyModel>>>()
    val properties: LiveData<DataState<List<PropertyModel>>> get() = _properties

    private val _childOptions = MutableLiveData<DataState<List<PropertyModel>>>()
    val childOptions: LiveData<DataState<List<PropertyModel>>> get() = _childOptions

    var childOptionsPosition: Int? = null



    // Fetches all main categories with null-safe data transformation
    fun fetchAllCategories() = viewModelScope.launch {
        fetchCategoriesUseCase().collect { result ->
            _allCategories.value = handleResult(result) { categories ->
                categories?.map { PropertyModel(it.id, it.name) }.orEmpty() // Null-safe list mapping
            }
        }
    }

    // Filters sub-categories by main category ID with null safety
    fun filterSubCategoriesByCategoryId(categoryId: Int) = viewModelScope.launch {
        filterSubCategoriesByCategoryIdUseCase(categoryId).collect { result ->
            _subCategories.value = handleResult(result) { subCategories ->
                val propertyList = subCategories?.map { PropertyModel(it.id, it.name) }.orEmpty()
                propertyList.toMutableList().apply { add(0, PropertyModel(-1, "اخري")) }
            }
        }
    }

    // Fetches properties of a sub-category with null safety
    fun fetchPropertiesBySubCategoryId(subCategoryId: Int) = viewModelScope.launch {
        fetchPropertiesUseCase(subCategoryId).collect { result ->
            _properties.value = handleResult(result) { properties ->
                properties?.map { property ->
                    PropertyModel(
                        property.id,
                        property.name,
                        property.options?.map { PropertyModel(it.id, it.name, hasChild =  it.hasChild) }.orEmpty()
                    )
                }.orEmpty()
            }
        }
    }

    // Fetches child options for a property based on selected option ID with null safety
    fun fetchOptionsChildByOptionId(optionId: Int) = viewModelScope.launch {
        fetchOptionsUseCase(optionId).collect { result ->
            _childOptions.value = handleResult(result) { options ->
                options?.map { option ->
                    PropertyModel(
                        option.id,
                        option.name,
                        option.options?.map { PropertyModel(it.id,it.name, hasChild = it.hasChild) }.orEmpty()
                    )
                }.orEmpty()


            }
        }
    }

    // Helper method to handle null safety and transform data if present
    private inline fun <T, R> handleResult(
        dataState: DataState<T>,
        transform: (T?) -> R
    ): DataState<R> {
        return when (dataState) {
            is DataState.Success -> DataState.Success(transform(dataState.data))
            is DataState.Error -> dataState // Error remains the same
            is DataState.Idle -> dataState // Error remains the same
            is DataState.Processing -> dataState // Error remains the same
            is DataState.ServerError -> dataState // Error remains the same
        }
    }
}
