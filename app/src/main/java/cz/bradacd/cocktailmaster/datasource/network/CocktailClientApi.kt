package cz.bradacd.cocktailmaster.datasource.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drink
import cz.bradacd.cocktailmaster.datasource.network.mapping.Drinks
import cz.bradacd.cocktailmaster.datasource.network.mapping.Ingredient
import cz.bradacd.cocktailmaster.datasource.network.mapping.Ingredients
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface CocktailApiCalls {
    @GET(APIConstants.SEARCH)
    suspend fun getDrinksByName(@Query("s") name: String): Drinks

    @GET(APIConstants.SEARCH)
    suspend fun getIngredientByName(@Query("i") name: String): Ingredients
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

    suspend fun fetchIngredientsByName(name: String): List<Ingredient> {
        val ingredient = retrofitService.getIngredientByName(name).ingredient
        if (ingredient.isNullOrEmpty()) {
            return emptyList()
        }
        return ingredient
    }
}