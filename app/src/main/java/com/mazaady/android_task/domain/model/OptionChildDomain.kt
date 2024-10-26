package com.mazaady.android_task.domain.model

// Domain Layer: Domain Models
data class OptionChildDomain(
    val id: Int,
    val name: String,
    val slug: String,
    val options: List<OptionDomainModel>
)