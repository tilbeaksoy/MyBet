package com.aksoy.mybet.listeners

import com.aksoy.mybet.models.CartModel

interface ICartLoadListener {
    fun onLoadCartSuccess(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message: String?)
}