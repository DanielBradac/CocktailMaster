package cz.bradacd.cocktailmaster.datasource.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.network.mapping.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiCalls {
    @GET(APIConstants.SEARCH)
    suspend fun getDrinksByName(@Query("s") name: String): DrinksDetailed

    @GET(APIConstants.SEARCH)
    suspend fun getIngredientByName(@Query("i") name: String): Ingredients

    @GET(APIConstants.FILTER)
    suspend fun getDrinksByIngredientName(@Query("i") name: String): Drinks

    @GET(APIConstants.FILTER)
    suspend fun getDrinksByCategory(@Query("a") name: String): Drinks
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

    suspend fun fetchDrinksByName(name: String): List<DetailedDrink> {
        return retrofitService.getDrinksByName(name).drinks
    }

    suspend fun fetchIngredientsByName(name: String): List<Ingredient> {
        val ingredient = retrofitService.getIngredientByName(name).ingredient
        if (ingredient.isNullOrEmpty()) {
            return emptyList()
        }
        return ingredient
    }

    suspend fun fetchIngredientsByCategory(category: DrinkCategory): List<Drink> {
        val drinks = retrofitService.getDrinksByCategory(category.apiName).drinks
        if (drinks.isNullOrEmpty()) {
            return emptyList()
        }
        return drinks
    }

    suspend fun fetchDrinksByIngredient(ingredientName: String): List<Drink> {
        val drinks = retrofitService.getDrinksByIngredientName(ingredientName).drinks
        Log.d("SearchDrinksViewModelLog", "I fetched by ingredient $ingredientName ${drinks.size}")
        Log.d("SearchDrinksViewModelLog", "Result: $drinks")
        if (drinks.isNullOrEmpty()) {
            return emptyList()
        }
        return drinks
    }
}