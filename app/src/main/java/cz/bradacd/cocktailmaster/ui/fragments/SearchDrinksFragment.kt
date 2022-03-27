package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import cz.bradacd.cocktailmaster.databinding.FragmentSearchDrinksBinding
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cz.bradacd.cocktailmaster.R

class SearchDrinksFragment : Fragment() {

    private lateinit var binding: FragmentSearchDrinksBinding
    private val viewModel: SearchDrinksViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchDrinksBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = this@SearchDrinksFragment
            viewModel = this@SearchDrinksFragment.viewModel
            searchButton.setOnClickListener {navigateToSearchRes()}
        }

        //viewModel.getDrinksByName("artini")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO zobrazit chybu a zablokovat online search, pokud je viewmodel.apiAvailable false
    }

    private fun navigateToSearchRes() {
        findNavController().navigate(R.id.action_searchDrinksFragment_to_searchResultFragment)
    }


}