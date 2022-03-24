package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.bradacd.cocktailmaster.databinding.FragmentSearchDrinksBinding
import cz.bradacd.cocktailmaster.viewmodel.SearchDrinksViewModel
import androidx.fragment.app.viewModels

class SearchDrinksFragment : Fragment() {

    private lateinit var binding: FragmentSearchDrinksBinding
    private val viewModel: SearchDrinksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchDrinksBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getDrinksByName("artini")
        return binding.root
    }
}