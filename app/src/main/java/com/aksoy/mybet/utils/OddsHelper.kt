package com.aksoy.mybet.utils

import com.aksoy.mybet.models.ModelForHelper

object OddsHelper {
        private var model = ModelForHelper()

        fun setKeyNTittle(key: String?, title: String) {
            model.sportsKey = key
            model.sportsTitle = key
        }
    fun get(): ModelForHelper {
        return model
    }
    }