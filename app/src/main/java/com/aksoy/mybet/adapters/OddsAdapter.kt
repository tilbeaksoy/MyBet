package com.aksoy.mybet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.models.response.Bookmakers
import com.aksoy.mybet.models.response.OddsResponse
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.rv_bet_odds_item.view.*


class OddsAdapter(
    val list: List<OddsResponse>) : RecyclerView.Adapter<OddsAdapter.OddsViewHolder>()
{
    class OddsViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    var homeTeam: String? = null
    var homeTeamOdd: String? = null
    var awayTeam: String? = null
    var awayTeamOdd: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OddsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_bet_odds_item, parent, false)
        return OddsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OddsViewHolder, position: Int) {
        val item = list[position]
        homeTeam = item.homeTeam
        holder.view.home_team.text = homeTeam
        awayTeam = item.awayTeam
        holder.view.away_team.text = awayTeam
        val bookmarkers : ArrayList<Bookmakers> = item.bookmakers
        for(item in bookmarkers){
            if(item.title.equals("FanDuel")){
                homeTeamOdd = item.markets[0].outcomes[1].price.toString()
                holder.view.home_team_odd.text = homeTeamOdd
                awayTeamOdd =  item.markets[0].outcomes[0].price.toString()
                holder.view.away_team_odd.text =awayTeamOdd
            }
        }

        holder.view.add_To_cart.setOnClickListener{
            addToCart()
        }
        }


    override fun getItemCount(): Int {
        return list.size
    }

    private fun addToCart() {
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String,Any?> = HashMap()
        var cartUid =  0
        cartUid = cartUid.inc()
        hashMap["homeTeam"] = homeTeam
        hashMap["awayTeam"] = awayTeam
        hashMap["homeTeamPrice"] = homeTeamOdd
        hashMap["awayTeamPrice"] = awayTeamOdd
        hashMap["timestamp"] = timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Cart")
        ref.child(cartUid.toString()).push()
            .setValue(hashMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener{ e ->

            }
    }
}