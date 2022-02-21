package com.aksoy.mybet.services

import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.models.response.SportsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("v4/sports/?")
    suspend fun getSports(): Response<ArrayList<SportsResponse>>

    @GET("v4/sports/{sport}/odds?regions=us&oddsFormat=decimal&markets=h2h")
    suspend fun getSportsOdds(@Path(value = "sport") sportKey: String): Response<ArrayList<OddsResponse>>
}