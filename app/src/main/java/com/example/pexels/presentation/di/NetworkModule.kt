package com.example.pexels.presentation.di

import android.content.Context
import androidx.work.WorkManager
import com.example.pexels.core.utils.Utils
import com.example.pexels.data.repoImpl.DownloadFileRepositoryImpl
import com.example.pexels.data.repository.Api
import com.example.pexels.data.repoImpl.PhotoRepositoryImpl
import com.example.pexels.domain.repository.DownloadFileRepository
import com.example.pexels.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun returnOkHTTPClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor { chain->
                val request=chain.request().newBuilder()
                    .addHeader("Authorization", Utils.API_KEY)
                    .build()
                chain.proceed(request)
            }.build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api{
        return retrofit.create(Api::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPhotoRepository(
        impl: PhotoRepositoryImpl
    ): PhotoRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDownloadModule {

    @Binds
    abstract fun bindDownloadRepository(
        impl: DownloadFileRepositoryImpl
    ): DownloadFileRepository

}
@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}