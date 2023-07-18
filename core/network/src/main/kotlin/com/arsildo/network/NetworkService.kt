package com.arsildo.network

import com.arsildo.model.ThreadCatalog
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {
    @GET
    suspend fun getThreadCatalog(@Url board: String): Response<List<ThreadCatalog>>

}