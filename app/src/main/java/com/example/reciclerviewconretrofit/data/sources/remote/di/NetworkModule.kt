package com.maxi.dogapi.di

import com.example.reciclerviewconretrofit.data.sources.remote.CustomerService
import com.example.reciclerviewconretrofit.data.sources.remote.OrderService
import com.example.recyclerviewenhanced.data.sources.remote.ServiceInterceptor
import com.example.recyclerviewenhanced.utils.Constants
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideServiceInterceptor(): ServiceInterceptor = ServiceInterceptor()


    @Singleton
    @Provides
    fun provideHttpClient(serviceInterceptor: ServiceInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(serviceInterceptor)
            .build()
    }

//    @Singleton
//    @Provides
//    fun provideConverterFactory(): GsonConverterFactory =
//        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideConverterMoshiFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(LocalDateTimeAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit): CustomerService =
        retrofit.create(CustomerService::class.java)

    @Singleton
    @Provides
    fun provideOrderService(retrofit: Retrofit): OrderService =
        retrofit.create(OrderService::class.java)
}


class LocalDateAdapter {

    @ToJson
    fun toJson(value: LocalDate): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

class LocalDateTimeAdapter {

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
