package com.mazaady.android_task.di

import com.mazaady.android_task.data.api.ApiService
import com.mazaady.android_task.data.repository.CategoryRepositoryImpl
import com.mazaady.android_task.domain.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://staging.mazaady.com/api/v1/"
    private const val PRIVATE_KEY = "3%o8i}_;3D4bF]G5@22r2)Et1&mLJ4?\$@+16"  // Replace with actual private key

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor to add the private-key header
        val headerInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("private-key", PRIVATE_KEY)  // Add the private-key header here

            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Logging interceptor for debugging
            .addInterceptor(headerInterceptor)   // Add the private-key header
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(apiService: ApiService): CategoryRepository {
        return CategoryRepositoryImpl(apiService)
    }
}
