package com.aksoy.mybet.listeners

import com.aksoy.mybet.models.OddModel

interface IOddsListLisitener {
    fun onBetLoadSuccess(betModelList:List<OddModel> )
    fun onBetLoadFailed(message: String)
}