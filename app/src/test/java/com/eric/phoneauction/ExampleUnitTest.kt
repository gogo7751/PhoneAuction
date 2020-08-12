package com.eric.phoneauction

import com.eric.phoneauction.auctionDialog.AuctionDialog
import com.eric.phoneauction.auctionDialog.AuctionViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val auctionViewModel = mockk<AuctionViewModel>()
    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun addMinimalPrice() {
        auctionViewModel.apply { event.value?.price = 100 }
        assertEquals(101, auctionViewModel.addMinimalPrice(100))
    }

    @Test
    fun addPrice() {
        auctionViewModel.event.value?.price = 100
        assertEquals(200, auctionViewModel.addMinimalPrice(100))
    }


}

