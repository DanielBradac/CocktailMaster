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
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.databinding.FragmentSearchResultBinding
import cz.bradacd.cocktailmaster.datasource.displayable.DisplayableDrink
import cz.bradacd.cocktailmaster.ui.adapters.SearchResultsRVAdapter
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel
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
        // Fetch data
        viewModel.initResultSearch(args)
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchResultFragment.viewModel

            // Adapter
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
                is LoadingStatus.Loading -> loadingDataFetch(status)
                is LoadingStatus.Success -> successFullDataFetch()
                is LoadingStatus.Error -> errorDataFetch(status)
            }
        }
    }

    private fun successFullDataFetch() {
        drinkList.clear()
        if (viewModel.drinks.value != null) {
            drinkList.addAll(viewModel.drinks.value!!)
        }
        binding.apply {
            // Fill recycler view with drinks
            searchResultRv.adapter!!.notifyItemRangeInserted(0, drinkList.size)
            // Fill the status bar.
            var statusTextFormat =
                String.format(getString(R.string.search_result_count), drinkList.size)
            if (drinkList.size >= onlineSearchLimit) {
                statusTextFormat += "\n" + getString(R.string.search_result_warning)
            }
            statusText.text = statusTextFormat
        }
    }

    private fun errorDataFetch(status: LoadingStatus.Error) {
        binding.statusText.text =
            String.format(getString(R.string.search_result_error), status.errorMessage)
    }

    private fun loadingDataFetch(status: LoadingStatus.Loading) {
        binding.statusText.text = getString(R.string.search_result_loading)
    }
}