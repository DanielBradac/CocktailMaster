package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.common.LoadingStatus
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailDataBinding
import cz.bradacd.cocktailmaster.viewmodel.DrinkDetailDataViewModel

class DrinkDetailDataFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailDataBinding
    private val args: DrinkDetailDataFragmentArgs by navArgs()

    private val viewModel: DrinkDetailDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.initDrinkDetailSearch(args)
        binding = FragmentDrinkDetailDataBinding.inflate(inflater)
        Log.d("CreatingLog", "Data fragment created with args: $args")
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

    private fun loadingDataFetch() {
        binding.statusBar.statusText.text = getString(R.string.search_result_loading)
    }

    private fun errorDataFetch(status: LoadingStatus.Error) {
        binding.statusBar.statusText.text = getString(status.errorMessageResource)
    }

    private fun successfulDataFetch() {
        binding.statusBar.statusText.visibility = View.GONE
    }

}
