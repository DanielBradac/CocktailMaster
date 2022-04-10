package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import cz.bradacd.cocktailmaster.databinding.FragmentSearchResultBinding
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.ui.adapters.SearchResultsRVAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel
import cz.bradacd.cocktailmaster.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding

    private val args: SearchResultFragmentArgs by navArgs()
    private val viewModel: SearchResultViewModel by viewModels()

    private var currentDrinkList = mutableListOf<DisplayableDrink>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fetch data
        viewModel.initResultSearch(args)
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchResultFragment.viewModel

            // Adapter
            searchResultRv.adapter = SearchResultsRVAdapter(
                currentDrinkList
            )
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Observe changes in fetched drinks and fill the adapter with it
        viewModel.drinks.observe(viewLifecycleOwner) {
            Log.d("FetchDebug", "Observer called")
            currentDrinkList.clear()
            if (viewModel.drinks.value != null) {
                Log.d("FetchDebug", "Got sum drinks: ${viewModel.drinks.value}")
                currentDrinkList.addAll(viewModel.drinks.value!!)
            }
            binding.searchResultRv.adapter!!
                .notifyItemRangeInserted(0, currentDrinkList.size)
        }
    }
}