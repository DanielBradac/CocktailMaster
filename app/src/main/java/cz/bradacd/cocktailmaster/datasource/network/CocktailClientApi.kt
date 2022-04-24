package cz.bradacd.cocktailmaster.datasource.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.datasource.network.mapping.*
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
    suspend fun getDrinksByCategory(@Query("a") category: String): Drinks

    @GET(APIConstants.LOOKUP)
    suspend fun getDrinkDetail(@Query("i") id: String): DrinksDetailed

    @GET(APIConstants.TEST)
    suspend fun testAPI(): APITest
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
        return retrofitService.getDrinksByName(name).drinks ?: return emptyList()
    }

    suspend fun fetchIngredientsByName(name: String): List<Ingredient> {
        return retrofitService.getIngredientByName(name).ingredient ?: return emptyList()
    }

    suspend fun fetchIngredientsByCategory(category: DrinkCategory): List<Drink> {
        return retrofitService.getDrinksByCategory(category.apiName).drinks?: return emptyList()
    }

    suspend fun fetchDrinksByIngredient(ingredientName: String): List<Drink> {
        return retrofitService.getDrinksByIngredientName(ingredientName).drinks
            ?: return emptyList()
    }

    suspend fun fetchDrinkDetailById(drinkId: String): DetailedDrink? {
        val drinks = retrofitService.getDrinkDetail(drinkId).drinks
        if (drinks.isNullOrEmpty()) {
            return null
        }
        return drinks[0]
    }

    suspend fun isAPIAvailable(): Boolean {
        val testCall = retrofitService.testAPI()
        return !testCall.entities.isNullOrEmpty()
    }
}