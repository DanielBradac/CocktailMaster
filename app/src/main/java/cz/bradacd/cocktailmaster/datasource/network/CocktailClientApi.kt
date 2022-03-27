package cz.bradacd.cocktailmaster.datasource.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drink
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drinks
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface CocktailApiCalls {
    @GET(APIConstants.SEARCH)
    suspend fun getDrinksByName(@Query("s") name: String): Drinks
}

object CocktailApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(APIConstants.BASE_URL)
        .build()

    private val retrofitService: CocktailApiCalls by lazy {
        retrofit.create(CocktailApiCalls::class.java)
    }

    suspend fun fetchDrinksByName(name: String): List<Drink> {
        return retrofitService.getDrinksByName(name).drinks
    }
}