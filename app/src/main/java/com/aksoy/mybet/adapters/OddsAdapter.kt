package com.aksoy.mybet.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.models.response.Bookmakers
import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.models.response.SportsResponse
import com.aksoy.mybet.utils.IRecyclerClickListener
import com.aksoy.mybet.utils.SelectListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.rv_bet_odds_item.view.*


class OddsAdapter(
    val list: List<OddsResponse>) :
    RecyclerView.Adapter<OddsAdapter.OddsViewHolder>()
{
    class OddsViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OddsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_bet_odds_item, parent, false)
        return OddsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OddsViewHolder, position: Int) {
        val item = list[position]
        holder.view.home_team.text = item.homeTeam
        holder.view.away_team.text = item.awayTeam
        val bookmarkers : ArrayList<Bookmakers> = item.bookmakers
        for(item in bookmarkers){
            if(item.title.equals("FanDuel")){
                holder.view.home_team_odd.text = item.markets.get(0).outcomes.get(1).price.toString()
                holder.view.away_team_odd.text = item.markets.get(0).outcomes.get(0).price.toString()
            }
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }
}