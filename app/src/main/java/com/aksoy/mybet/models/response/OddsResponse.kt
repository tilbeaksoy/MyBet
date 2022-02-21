package com.aksoy.mybet.models.response

import com.google.gson.annotations.SerializedName

data class OddsResponse (

    @SerializedName("id"            ) var id           : String?               = null,
    @SerializedName("sport_key"     ) var sportKey     : String?               = null,
    @SerializedName("sport_title"   ) var sportTitle   : String?               = null,
    @SerializedName("commence_time" ) var commenceTime : String?               = null,
    @SerializedName("home_team"     ) var homeTeam     : String?               = null,
    @SerializedName("away_team"     ) var awayTeam     : String?               = null,
    @SerializedName("bookmakers"    ) var bookmakers   : ArrayList<Bookmakers> = arrayListOf()

)
data class Outcomes (

    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("price" ) var price : Double? = null

)
data class Markets (

    @SerializedName("key"      ) var key      : String?             = null,
    @SerializedName("outcomes" ) var outcomes : ArrayList<Outcomes> = arrayListOf()

)
data class Bookmakers (

    @SerializedName("key"         ) var key        : String?            = null,
    @SerializedName("title"       ) var title      : String?            = null,
    @SerializedName("last_update" ) var lastUpdate : String?            = null,
    @SerializedName("markets"     ) var markets    : ArrayList<Markets> = arrayListOf()

)