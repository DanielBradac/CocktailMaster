package cz.bradacd.cocktailmaster.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.bradacd.cocktailmaster.databinding.CocktailListItemBinding
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink

class SearchResultsRVAdapter(
    private val drinks: List<DisplayableDrink>
): RecyclerView.Adapter<SearchResultsRVAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(CocktailListItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("FetchDebug", "Filling up RV with ${drinks[position]}")
        holder.bind(drinks[position])
    }

    override fun getItemCount(): Int = drinks.size

    class ItemViewHolder(private var binding: CocktailListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: DisplayableDrink) {
            binding.drink = drink
        }
    }
}