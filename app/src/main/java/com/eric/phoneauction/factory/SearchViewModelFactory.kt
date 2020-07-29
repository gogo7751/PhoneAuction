package com.eric.phoneauction.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.searchFragment.SearchViewModel

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory constructor(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val search: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(phoneAuctionRepository, search)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}



