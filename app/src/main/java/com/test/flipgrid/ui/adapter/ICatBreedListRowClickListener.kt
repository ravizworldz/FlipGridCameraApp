package com.test.flipgrid.ui.adapter

import com.test.flipgrid.models.CatBreedListItem

interface ICatBreedListRowClickListener {
    fun onCatBreedRowClick(catBreedListItem: CatBreedListItem)
}