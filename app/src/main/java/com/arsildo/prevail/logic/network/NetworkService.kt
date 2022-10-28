package com.arsildo.prevail.logic.network

import com.arsildo.prevail.logic.network.model.thread_catalog.ThreadCatalogItem
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkService {

    @GET
    suspend fun getCatalog(@Url board: String): List<ThreadCatalogItem>

}