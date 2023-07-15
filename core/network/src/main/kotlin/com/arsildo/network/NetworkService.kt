package com.arsildo.network

import com.arsildo.model.ThreadCatalog
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {
    @GET
    suspend fun getThreadCatalog(@Url board: String): List<ThreadCatalog>

}