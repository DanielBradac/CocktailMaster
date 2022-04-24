package cz.bradacd.cocktailmaster.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.bradacd.cocktailmaster.ui.fragments.DrinkDetailDataFragment
import cz.bradacd.cocktailmaster.ui.fragments.DrinkDetailNotesFragment

class DrinkDetailPagesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    // 2 tabs - Drink page and Notes page
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = when (position) {
        1 -> DrinkDetailNotesFragment()
        else -> DrinkDetailDataFragment()
    }
}

