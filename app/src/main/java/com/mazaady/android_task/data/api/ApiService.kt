package com.mazaady.android_task.data.api

import com.mazaady.android_task.data.model.CategoryResponse
import com.mazaady.android_task.data.model.OptionsResponse
import com.mazaady.android_task.data.model.PropertiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("get_all_cats")
    suspend fun fetchAllCategories(): Response<CategoryResponse?>

    @GET("properties")
    suspend fun fetchPropertiesBySubCategoryId(@Query("cat") subCategoryId: Int): Response<PropertiesResponse?>

    @GET("get-options-child/{optionId}")
    suspend fun fetchOptionsChild(@Path("optionId") optionId: Int): Response<OptionsResponse?>
}
