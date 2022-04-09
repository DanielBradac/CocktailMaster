package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cz.bradacd.cocktailmaster.databinding.FragmentSearchResultBinding
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel
import cz.bradacd.cocktailmaster.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding

    val args: SearchResultFragmentArgs by navArgs()
    private val viewModel: SearchResultViewModel by viewModels()

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
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.viewModel.drinks.observe(viewLifecycleOwner) {
            Log.d("LookingForDrinks", "Brother I fetched sum drinks: count: ${viewModel.drinks.value?.size}" +
                    ", drinks: ${viewModel.drinks.value}")
        }
    }
}