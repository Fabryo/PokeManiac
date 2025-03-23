package com.shodo.android.api.core.remote

import com.shodo.android.api.BuildConfig
import com.shodo.android.api.core.remote.model.SearchCharacterDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SuperHerosApiService {

    @GET(PATH_SEARCH_CHARACTER)
    suspend fun searchCharacter(@Path("superHeroName") superHeroName: String): Response<SearchCharacterDTO>

    companion object {
        private const val PATH_SEARCH_CHARACTER = "/api/${BuildConfig.apiToken}/search/{superHeroName}"
    }
}
