package cz.bradacd.cocktailmaster.ui.fragments

import android.R.layout.simple_list_item_1
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textview.MaterialTextView
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.databinding.FragmentSearchDrinksBinding
import cz.bradacd.cocktailmaster.ui.adapters.AddedIngredientAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel

// TODO setupnout constrint layout v constraint layoutu - ten vnější bude řešit ty okraje, případně v něm bude to search tlačítko
// TODO tím využijeme celou plochu obrazovky pro ingredience

class SearchDrinksFragment : Fragment() {

    private lateinit var binding: FragmentSearchDrinksBinding
    private val viewModel: SearchDrinksViewModel by viewModels()
    private var addedIngredientsList: MutableList<String> = mutableListOf()
    private lateinit var suggestionAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchDrinksBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = this@SearchDrinksFragment
            viewModel = this@SearchDrinksFragment.viewModel

            // Adapters setup
            suggestionAdapter = createAutocompleteAdapter()
            addIngredientsAutocomplete.setAdapter(suggestionAdapter)

            addedIngredientsRw.adapter = AddedIngredientAdapter(addedIngredientsList)
            addedIngredientsRw.layoutManager =
                GridLayoutManager(this@SearchDrinksFragment.requireContext(), 2)

            // Listeners setup
            searchButton.setOnClickListener {navigateToSearchRes()}
            addIngredientsAutocomplete.addTextChangedListener(createTextWatcher())
            addIngredientsAutocomplete.onItemClickListener =
                AdapterView.OnItemClickListener { _, clickedView, _, _ ->
                    if (clickedView is MaterialTextView) {
                        addIngredient(clickedView.text.toString())
                    }
                }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO zobrazit chybu a zablokovat online search, pokud je viewmodel.apiAvailable false

        // Observers setup
        this.viewModel.ingredientSuggestion.observe(viewLifecycleOwner) { newSuggestions ->
            if (newSuggestions.isNullOrEmpty()) {
                binding.addIngredientsAutocomplete.error = "No ingredients found"
            }
            //Update suggestions
            suggestionAdapter.apply {
                clear()
                addAll(newSuggestions)
                // We show whole new list, because it is already filtered from the data fetch
                suggestionAdapter.filter.filter(null)
            }
        }
    }

    private fun navigateToSearchRes() {
        findNavController().navigate(R.id.action_searchDrinksFragment_to_searchResultFragment)
    }

    private fun createAutocompleteAdapter(): ArrayAdapter<String> =
        ArrayAdapter<String>(
            this.requireContext(),
            simple_list_item_1,
            mutableListOf()
        )

    private fun addIngredient(ingredientText: String) {
        with (binding) {
            addedIngredientsList.add(ingredientText)
            addedIngredientsRw.adapter!!.notifyItemInserted(addedIngredientsList.size - 1)
            addIngredientsAutocomplete.setText("")
        }
    }

    private fun createTextWatcher() = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val ingredientAlreadyFetched =
                viewModel.ingredientSuggestion.value != null &&
                viewModel.ingredientSuggestion.value is List<String> &&
                viewModel.ingredientSuggestion.value!!.contains(s.toString())

            if (!s.isNullOrBlank() &&
                s.length >= binding.addIngredientsAutocomplete.threshold &&
                !ingredientAlreadyFetched) {
                viewModel.updateSuggestionList(s.toString())
            } else {
                binding.addIngredientsAutocomplete.error = null
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}