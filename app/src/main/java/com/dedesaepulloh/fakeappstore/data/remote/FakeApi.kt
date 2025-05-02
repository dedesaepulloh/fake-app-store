package com.dedesaepulloh.fakeappstore.data.remote

import com.dedesaepulloh.fakeappstore.data.model.LoginRequest
import com.dedesaepulloh.fakeappstore.data.model.LoginResponse
import com.dedesaepulloh.fakeappstore.data.model.ProductResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FakeApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponseItem>>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): Response<ProductResponseItem>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("/products/category/{category}")
    suspend fun getProductByCategory(@Path("category") category: String): Response<List<ProductResponseItem>>

}