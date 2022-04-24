package cz.bradacd.cocktailmaster.ui.fragments

import android.R.layout.simple_list_item_1
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textview.MaterialTextView
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.DrinkCategory
import cz.bradacd.cocktailmaster.databinding.FragmentSearchDrinksBinding
import cz.bradacd.cocktailmaster.ui.adapters.AddedIngredientRVAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel

// TODO setupnout constrint layout v constraint layoutu - ten vnější bude řešit ty okraje, případně v něm bude to search tlačítko
// TODO - případně se dá řešit pomocí android:layout_margin
// TODO tím využijeme celou plochu obrazovky pro ingredience

class SearchDrinksFragment : Fragment() {

    private lateinit var binding: FragmentSearchDrinksBinding
    private val viewModel: SearchDrinksViewModel by viewModels()
    private var addedIngredientsList = mutableListOf<String>()
    private lateinit var suggestionAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchDrinksBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchDrinksFragment.viewModel

            // Adapters setup
            suggestionAdapter = createAutocompleteAdapter()
            addIngredientsAutocomplete.setAdapter(suggestionAdapter)

            addedIngredientsRv.adapter = AddedIngredientRVAdapter(addedIngredientsList)
            addedIngredientsRv.layoutManager =
                GridLayoutManager(this@SearchDrinksFragment.requireContext(), 2)

            drinkTypeSpinner.adapter =
                ArrayAdapter(
                    this@SearchDrinksFragment.requireContext(),
                    R.layout.spinner_item,
                    DrinkCategory.values()
                )

            // Listeners setup
            searchButton.setOnClickListener { navigateToSearchRes() }
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
        // Observe changes in ingredients suggestions data and change suggestion accordingly
        viewModel.ingredientSuggestion.observe(viewLifecycleOwner) { newSuggestions ->
            if (newSuggestions.isNullOrEmpty()) {
                binding.addIngredientsAutocomplete.error = "No ingredients found"
            }
            //Update suggestions
            suggestionAdapter.apply {
                clear()
                addAll(newSuggestions)
                // We show whole new list, because it is already filtered from the data fetch
                filter.filter(null)
            }
        }
        // Observe if the API is available and block online search if it is not
        viewModel.apiAvailable.observe(viewLifecycleOwner) { apiAvailable ->
            binding.onlineSwitch.isEnabled = true
            binding.statusBar.root.visibility = View.GONE
            if (!apiAvailable) {
                binding.onlineSwitch.isChecked = false
                binding.onlineSwitch.isEnabled = false
                binding.statusBar.statusText.text = getString(R.string.error_internet_connection)
                binding.statusBar.root.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToSearchRes() {
        with(binding) {
            val action =
                SearchDrinksFragmentDirections.actionSearchDrinksFragmentToSearchResultFragment(
                    drinkTypeSpinner.selectedItem.toString(),
                    cocktailNameText.text.toString(),
                    addedIngredientsList.toTypedArray(),
                    localSwitch.isChecked,
                    onlineSwitch.isChecked
                )
            findNavController().navigate(action)
        }
    }

    private fun createAutocompleteAdapter(): ArrayAdapter<String> =
        ArrayAdapter<String>(
            this.requireContext(),
            simple_list_item_1,
            mutableListOf()
        )

    private fun addIngredient(ingredientText: String) {
        with(binding) {
            addedIngredientsList.add(ingredientText)
            addedIngredientsRv.adapter!!.notifyItemInserted(addedIngredientsList.size - 1)
            addIngredientsAutocomplete.setText("")
        }
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val ingredientAlreadyFetched =
                viewModel.ingredientSuggestion.value != null &&
                        viewModel.ingredientSuggestion.value is List<String> &&
                        viewModel.ingredientSuggestion.value!!.contains(s.toString())

            if (!s.isNullOrBlank() &&
                s.length >= binding.addIngredientsAutocomplete.threshold &&
                !ingredientAlreadyFetched
            ) {
                viewModel.updateSuggestionList(s.toString())
            } else {
                binding.addIngredientsAutocomplete.error = null
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}