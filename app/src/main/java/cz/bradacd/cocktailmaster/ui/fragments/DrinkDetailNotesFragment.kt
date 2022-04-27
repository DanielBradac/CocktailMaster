package cz.bradacd.cocktailmaster.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import cz.bradacd.cocktailmaster.databinding.FragmentDrinkDetailNotesBinding

class DrinkDetailNotesFragment : Fragment() {
    private lateinit var binding: FragmentDrinkDetailNotesBinding
    private val args: DrinkDetailNotesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CreatingLog", "Notes fragment created with args: $args")
        binding = FragmentDrinkDetailNotesBinding.inflate(inflater)
        return binding.root
    }

}