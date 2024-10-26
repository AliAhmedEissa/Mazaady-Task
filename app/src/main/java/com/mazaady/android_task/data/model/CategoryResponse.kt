package com.mazaady.android_task.data.model

data class CategoryResponse(
    val code: Int? = null,
    val msg: String? = null,
    val data: CategoryData? = null
)

data class CategoryData(
    val categories: List<Category>? = null,
    val ios_version: String? = null,
    val ios_latest_version: String? = null,
    val google_version: String? = null,
    val huawei_version: String? = null
)

data class Category(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
    val slug: String? = null,
    val children: List<SubCategory>? = null,
    val circle_icon: String? = null,
    val disable_shipping: Int? = null
)

data class SubCategory(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
    val slug: String? = null,
    val children: List<SubCategory>? = null,
    val circle_icon: String? = null,
    val disable_shipping: Int? = null
)
