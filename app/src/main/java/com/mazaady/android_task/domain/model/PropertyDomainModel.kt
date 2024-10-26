package com.mazaady.android_task.domain.model

data class PropertyDomainModel(
    val id: Int,
    val name: String,
    val slug: String,
    val options: List<OptionDomainModel>
)
