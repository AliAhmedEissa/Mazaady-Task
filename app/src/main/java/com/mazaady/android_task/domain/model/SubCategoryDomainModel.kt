package com.mazaady.android_task.domain.model

data class SubCategoryDomainModel(
    val id: Int,
    val name: String,
    val slug: String,
    val categoryId: Int,
)