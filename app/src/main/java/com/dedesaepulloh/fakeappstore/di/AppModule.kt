package com.dedesaepulloh.fakeappstore.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dedesaepulloh.fakeappstore.BuildConfig
import com.dedesaepulloh.fakeappstore.data.local.FakeDao
import com.dedesaepulloh.fakeappstore.data.local.FakeDatabase
import com.dedesaepulloh.fakeappstore.data.local.datastore.UserPreferences
import com.dedesaepulloh.fakeappstore.data.remote.FakeApi
import com.dedesaepulloh.fakeappstore.data.repository.FakeRepositoryImpl
import com.dedesaepulloh.fakeappstore.domain.repository.FakeRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context
    ): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .addInterceptor(ChuckerInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideCartDao(@ApplicationContext context: Context): FakeDao {
        return Room.databaseBuilder(
            context,
            FakeDatabase::class.java,
            "product_db"
        ).fallbackToDestructiveMigration().build().fakeDao()
    }

    @Provides
    @Singleton
    fun provideFakeApi(retrofit: Retrofit): FakeApi {
        return retrofit.create(FakeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(fakeApi: FakeApi, fakeDao: FakeDao, userPreferences: UserPreferences): FakeRepository {
        return FakeRepositoryImpl(fakeApi, fakeDao, userPreferences)
    }

    @Provides
    @Singleton
    fun provideFakeDataStoreManager(@ApplicationContext context: Context, gson: Gson): UserPreferences {
        return UserPreferences(context, gson)
    }

}