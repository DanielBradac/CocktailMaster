package cz.bradacd.cocktailmaster.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.bradacd.cocktailmaster.ui.fragments.DrinkDetailDataFragment
import cz.bradacd.cocktailmaster.ui.fragments.DrinkDetailNotesFragment


class DrinkDetailPagesAdapter(
    val fragment: Fragment,
    private val drinkId: String,
    private val dataSourceTag: String
) : FragmentStateAdapter(fragment) {
    // 2 tabs - Drink page and Notes page
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // I don't know how to pass this safely, since I cannot do it via action in nav. graph
        val args = Bundle()
        args.putString("drinkId", drinkId)
        args.putString("dataSourceTag", dataSourceTag)

        val fragment = when (position) {
            1 -> DrinkDetailNotesFragment()
            else -> DrinkDetailDataFragment()
        }
        fragment.arguments = args
        return fragment
    }
}

