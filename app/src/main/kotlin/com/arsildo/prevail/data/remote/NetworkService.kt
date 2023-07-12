package com.arsildo.prevail.data.remote

import com.arsildo.prevail.data.ThreadCatalogItem
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {
    @GET
    suspend fun getThreadCatalog(@Url board: String): List<ThreadCatalogItem>

}