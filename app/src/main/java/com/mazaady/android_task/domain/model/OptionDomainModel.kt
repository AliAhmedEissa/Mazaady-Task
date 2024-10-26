package com.mazaady.android_task.domain.model

data class OptionDomainModel(
    val id: Int,
    val name: String,
    val slug: String,
    val subCategoryId:Int,
    val hasChild:Boolean,
)
