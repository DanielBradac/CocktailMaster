package cz.bradacd.cocktailmaster.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cz.bradacd.cocktailmaster.databinding.CocktailListItemBinding
import cz.bradacd.cocktailmaster.datasource.browser.Browser
import cz.bradacd.cocktailmaster.datasource.browser.MultipleSourceDataCollector
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.datasource.network.CocktailAPIBrowser
import cz.bradacd.cocktailmaster.ui.fragments.SearchResultFragmentArgs
import cz.bradacd.cocktailmaster.ui.fragments.SearchResultFragmentDirections

class SearchResultsRVAdapter(
    private val drinks: List<DisplayableDrink>,
    args: SearchResultFragmentArgs
): RecyclerView.Adapter<SearchResultsRVAdapter.ItemViewHolder>() {

    private var dataCollector: MultipleSourceDataCollector

    init {
        // We need data collector for collecting images
        val browsers = mutableListOf<Browser>()

        // TODO DBBrowser not yet implemented
        //if (args.searchLocal) browsers.add(DBBrowser())
        if (args.searchOnline) browsers.add(CocktailAPIBrowser())
        dataCollector = MultipleSourceDataCollector(browsers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(CocktailListItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("FetchDebug", "Filling up RV with ${drinks[position]}")
        holder.bind(drinks[position], dataCollector)

        // Navigate to drink detail on click
        val action =
            SearchResultFragmentDirections.actionSearchResultFragmentToDrinkDetailFragment(
                drinks[position].id,
                drinks[position].dataSourceTag
            )
        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = drinks.size

    class ItemViewHolder(private var binding: CocktailListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: DisplayableDrink, dataCollector: MultipleSourceDataCollector) {
            binding.drink = drink
            binding.dataCollector = dataCollector
        }
    }
}