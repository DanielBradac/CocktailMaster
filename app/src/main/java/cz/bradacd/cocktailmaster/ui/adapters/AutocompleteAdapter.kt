package cz.bradacd.cocktailmaster.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.bradacd.cocktailmaster.R

class AddedIngredientAdapter (
    private val addedIngredients: MutableList<String>
): RecyclerView.Adapter<AddedIngredientAdapter.ItemViewHolder>() {

    val logTag = "AddedIngredientAdapterLog"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(logTag, "I now have ${addedIngredients.size} items")
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.added_ingredients_list, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(logTag, "I now have ${addedIngredients.size} items")
        holder.apply {
            ingredientText.text = addedIngredients[position]
            // Remove item
            removeIngredientBtn.setOnClickListener {
                addedIngredients.removeAt(position)
                this@AddedIngredientAdapter.notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int = addedIngredients.size

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ingredientText: TextView = view.findViewById(R.id.added_ingredient)
        val removeIngredientBtn: ImageButton = view.findViewById(R.id.remove_ingredient)
    }
}