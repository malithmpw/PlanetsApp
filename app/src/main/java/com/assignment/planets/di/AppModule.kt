package com.assignment.planets.di

import android.app.Application
import androidx.room.Room
import com.assignment.planets.BuildConfig
import com.assignment.planets.data.repository.PlanetsRepository
import com.assignment.planets.data.repository.PlanetsRepositoryImpl
import com.assignment.planets.data.repository.local.PlanetsDatabase
import com.assignment.planets.data.repository.remote.PlanetsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesPlanetsApi(): PlanetsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
            .create(PlanetsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPlanetsDatabase(app: Application): PlanetsDatabase {
        return Room.databaseBuilder(
            app,
            PlanetsDatabase::class.java,
            "planets.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlanetsRepository(
        planetsApi: PlanetsApi,
        planetsDatabase: PlanetsDatabase
    ): PlanetsRepository {
        return PlanetsRepositoryImpl(planetsApi, planetsDatabase)
    }
}