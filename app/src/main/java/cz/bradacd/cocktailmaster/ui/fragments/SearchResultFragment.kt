package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.databinding.FragmentSearchResultBinding
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.ui.adapters.SearchResultsRVAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding

    private val args: SearchResultFragmentArgs by navArgs()
    private val viewModel: SearchResultViewModel by viewModels()

    private var drinkList = mutableListOf<DisplayableDrink>()
    private val onlineSearchLimit = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Start search
        viewModel.initDrinkListSearch(args)

        binding = FragmentSearchResultBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchResultFragment.viewModel

            // Adapter
            Log.d("DebuggingCrash", "I am creating adapter with: ${drinkList}")
            searchResultRv.adapter = SearchResultsRVAdapter(
                drinkList,
                args
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Observe changes in data fetch status and fill the adapter if it was successful
        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is LoadingStatus.Loading -> loadingDataFetch()
                is LoadingStatus.Success -> successfulDataFetch()
                is LoadingStatus.Error -> errorDataFetch(status)
            }
        }
    }

    private fun successfulDataFetch() {
        // If the drink list is the same, we don't need to update it
        if (drinkList != viewModel.drinks) {
            // Fill adapter with new values
            drinkList.clear()
            drinkList.addAll(viewModel.drinks)
            binding.searchResultRv.adapter!!.notifyItemRangeInserted(0, drinkList.size)
        }

        // Update status bar text with result
        binding.statusBar.statusText.text = String.format(getString(R.string.search_result_count), drinkList.size)
        if (drinkList.size >= onlineSearchLimit) {
            binding.statusBar.warningText.text = getString(R.string.search_result_warning)
            binding.statusBar.warningText.visibility = View.VISIBLE
        }
    }

    private fun errorDataFetch(status: LoadingStatus.Error) {
        binding.statusBar.statusText.text = getString(status.errorMessageResource)
    }

    private fun loadingDataFetch() {
        binding.statusBar.statusText.text = getString(R.string.search_result_loading)
    }
}