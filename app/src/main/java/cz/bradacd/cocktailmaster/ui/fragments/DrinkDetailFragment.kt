package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailBinding
import cz.bradacd.cocktailmaster.ui.adapters.DrinkDetailPagesAdapter
import cz.bradacd.cocktailmaster.viewmodel.DrinkDetailViewModel

class DrinkDetailFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailBinding
    private lateinit var pagesAdapter: DrinkDetailPagesAdapter

    private val args: DrinkDetailFragmentArgs by navArgs()
    private val viewModel: DrinkDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Start search for drink detail
        viewModel.initDrinkDetailSearch(args)

        //binding = FragmentDrinkDetailBinding.inflate(inflater)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drink_detail, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DrinkDetailFragment.viewModel
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

        // Init adapter for the two pages
        pagesAdapter = DrinkDetailPagesAdapter(this)
        binding.pager.adapter = pagesAdapter

        // Add tabs with titles for pages
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when(position) {
                1 -> getString(R.string.drink_detail_notes_tab)
                else -> getString(R.string.drink_detail_info_tab)
            }
        }.attach()

    }

    private fun loadingDataFetch() {
        //binding.statusText.text = getString(R.string.search_result_loading)
    }

    private fun errorDataFetch(status: LoadingStatus.Error) {
        //binding.statusText.text = getString(status.errorMessageResource)
    }

    private fun successfulDataFetch() {
        //binding.statusText.visibility = View.GONE
    }
}