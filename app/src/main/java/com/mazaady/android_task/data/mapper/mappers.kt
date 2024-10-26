package com.mazaady.android_task.data.mapper

import com.mazaady.android_task.data.model.CategoryData
import com.mazaady.android_task.data.model.OptionChildData
import com.mazaady.android_task.data.model.OptionsItem
import com.mazaady.android_task.data.model.OptionsResponse
import com.mazaady.android_task.data.model.Property
import com.mazaady.android_task.data.model.SubCategory
import com.mazaady.android_task.domain.model.CategoryDomainModel
import com.mazaady.android_task.domain.model.OptionChildDomain
import com.mazaady.android_task.domain.model.OptionDomainModel
import com.mazaady.android_task.domain.model.PropertyDomainModel
import com.mazaady.android_task.domain.model.SubCategoryDomainModel


// Extension function to map SubCategory to SimplifiedSubCategory
fun SubCategory.toSubCategoryDomainModel(categoryId: Int): SubCategoryDomainModel {
    return SubCategoryDomainModel(
        id = this.id?:0,
        name = this.name?:"",
        categoryId = categoryId,
        slug = this.slug?:"",
    )
}

// Extension function to map CategoryData to a list of SimplifiedCategory
fun CategoryData.toCategoriesDomainModel(): List<CategoryDomainModel> {
    return categories?.map { category ->
        CategoryDomainModel(
            id = category.id?:0,
            name = category.name?:"",
            slug = category.slug?:"",
            children = category.children?.map { child ->
                child.toSubCategoryDomainModel(category.id?:-1)
            } ?: emptyList()
        )
    }!!
}


// Extension function to map OptionsItem to OptionDomainModel
fun OptionsItem.toOptionDomainModel(): OptionDomainModel {
    return OptionDomainModel(
        id = this.id ?: 0, // Provide default values for null fields
        name = this.name ?: "",
        slug = this.slug ?: "",
        subCategoryId = this.parent?:0,
        hasChild = this.child?:false
    )
}


// Extension function to map Property to PropertyDomainModel
fun Property.toPropertyDomainModel(): PropertyDomainModel {
    return PropertyDomainModel(
        id = this.id ?: 0, // Provide default values for null fields
        name = this.name ?: "",
        slug = this.slug ?: "",
        options = this.options?.mapNotNull { it?.toOptionDomainModel() } ?: emptyList()
    )
}

// Extension function to map a list of Property to a list of PropertyDomainModel
fun List<Property?>.toPropertyDomainModels(): List<PropertyDomainModel> {
    return this.mapNotNull { it?.toPropertyDomainModel() } // map each property to domain model, ignoring nulls
}


// Data Layer or Mapper Utility: Extension Function for Mapping
fun OptionChildData.toOptionChildDomain(): OptionChildDomain {
    return OptionChildDomain(
        id = this.id ?: 0,
        name = this.name ?: "",
        slug = this.slug ?: "",
        options = this.options?.map { option ->
            OptionDomainModel(
                id = option.id ?: 0,
                name = option.name ?: "",
                slug = option.slug ?: "",
                subCategoryId = option.parent ?: 0,
                hasChild =false
            )
        } ?: emptyList()
    )
}

// Map the full response list
fun OptionsResponse.toDomainList(): List<OptionChildDomain> {
    return this.data?.map { it.toOptionChildDomain() } ?: emptyList()
}