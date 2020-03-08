package com.example.recipe

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel() {

    var navDrawerDisplaySelection = R.id.nav_recipes
    val navDrawerSelectionName = "com.example.recipe.ItemsActivityViewModel.navDrawerDisplaySelection"

    fun saveState(outState: Bundle) {
        outState.putInt(navDrawerSelectionName, navDrawerDisplaySelection)

    }

    fun restoreState(savedInstanceState: Bundle) {
            navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerSelectionName)
        }
    }