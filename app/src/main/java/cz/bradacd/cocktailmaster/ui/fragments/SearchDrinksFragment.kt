package cz.bradacd.cocktailmaster.ui.fragments

import android.R.layout.simple_list_item_1
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.databinding.FragmentSearchDrinksBinding
import cz.bradacd.cocktailmaster.ui.adapters.AddedIngredientAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel

// TODO setupnout constrint layout v constraint layoutu - ten vnější bude řešit ty okraje, případně v něm bude to search tlačítko
// TODO tím využijeme celou plochu obrazovky pro ingredience
// TODO ingredience actually skládat dolů, aby se maximálně využilo místo

class SearchDrinksFragment : Fragment() {

    private lateinit var binding: FragmentSearchDrinksBinding
    private val viewModel: SearchDrinksViewModel by viewModels()
    private var addedIngredientsList: MutableList<String> = mutableListOf()
    private val logTag ="SearchDrinksFragmentLog"

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
            addIngredientsAutocomplete.setAdapter(createAutocompleteAdapter())
            addedIngredientsRw.adapter = AddedIngredientAdapter(addedIngredientsList)
            addedIngredientsRw.setLayoutManager(GridLayoutManager(this@SearchDrinksFragment.requireContext(), 3))

            // Listeners setup
            searchButton.setOnClickListener {navigateToSearchRes()}
            addIngredientButton.setOnClickListener {addIngredient()}
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO zobrazit chybu a zablokovat online search, pokud je viewmodel.apiAvailable false
    }

    private fun navigateToSearchRes() {
        findNavController().navigate(R.id.action_searchDrinksFragment_to_searchResultFragment)
    }

    private fun createAutocompleteAdapter(): ArrayAdapter<String>  {
        return ArrayAdapter(
            this.requireContext(),
            simple_list_item_1,
            arrayListOf("Belgium", "France", "Italy", "Germany", "Spain")
        )
    }

    private fun addIngredient() {
        binding.apply {
            addedIngredientsList.add(addIngredientsAutocomplete.text.toString())
            addedIngredientsRw.adapter!!.notifyDataSetChanged()
            addIngredientsAutocomplete.setText("")
        }
    }
}