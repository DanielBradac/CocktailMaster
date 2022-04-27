package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.bradacd.cocktailmaster.R
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailBinding
import cz.bradacd.cocktailmaster.ui.adapters.DrinkDetailPagesAdapter

class DrinkDetailFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailBinding
    private lateinit var pagesAdapter: DrinkDetailPagesAdapter

    private val args: DrinkDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Start search for drink detail
        Log.d("CreatingLog", "Main fragment created")
        binding = FragmentDrinkDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Init adapter for the two pages
        pagesAdapter = DrinkDetailPagesAdapter(this, args.drinkId, args.dataSourceTag)
        binding.pager.adapter = pagesAdapter

        // Add tabs with titles for pages
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when(position) {
                1 -> getString(R.string.drink_detail_notes_tab)
                else -> getString(R.string.drink_detail_info_tab)
            }
        }.attach()
    }
}