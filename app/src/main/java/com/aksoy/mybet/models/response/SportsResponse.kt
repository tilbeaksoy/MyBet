package com.aksoy.mybet.models.response

import com.google.gson.annotations.SerializedName




data class SportsResponse(

        @SerializedName("key")
        val key: String,
        @SerializedName("group")
        val group: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("active")
        val active: Boolean,
        @SerializedName("has_outrights")
        val has_outrights: Boolean
)