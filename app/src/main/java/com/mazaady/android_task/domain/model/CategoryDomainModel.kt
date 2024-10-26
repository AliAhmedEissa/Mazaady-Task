package com.mazaady.android_task.domain.model

data class CategoryDomainModel(
    val id: Int,
    val name: String,
    val slug: String,
    val children: List<SubCategoryDomainModel>
)

