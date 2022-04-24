package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailDataBinding

class DrinkDetailDataFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrinkDetailDataBinding.inflate(inflater)
        return binding.root
    }
}