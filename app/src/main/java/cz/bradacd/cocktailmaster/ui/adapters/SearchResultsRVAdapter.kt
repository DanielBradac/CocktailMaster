package cz.bradacd.cocktailmaster.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink

class SearchResultsRVAdapter(
    private val drinks: List<DisplayableDrink>
): RecyclerView.Adapter<SearchResultsRVAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.cocktail_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("FetchDebug", "Filling up RV with ${drinks[position].name}")
        holder.apply {
            cocktailName.text = drinks[position].name
            cocktailImage.setImageResource(R.drawable.ic_cocktail_icon)
        }
    }

    override fun getItemCount(): Int = drinks.size

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cocktailName: TextView = view.findViewById(R.id.cocktail_name_listItem)
        val cocktailImage: ImageView = view.findViewById(R.id.cocktail_image_listItem)
    }
}