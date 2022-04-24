package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailNotesBinding

class DrinkDetailNotesFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrinkDetailNotesBinding.inflate(inflater)
        return binding.root
    }

}