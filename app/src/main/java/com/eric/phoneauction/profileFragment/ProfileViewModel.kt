package com.eric.phoneauction.profileFragment

import androidx.lifecycle.ViewModel
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository

class ProfileViewModel(
     val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {


     val user = UserManager.user




}
