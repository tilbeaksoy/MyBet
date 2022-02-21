package com.aksoy.mybet.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.eventbus.UpdateCartEvent
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.models.OddModel
import com.aksoy.mybet.models.response.Bookmakers
import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.utils.AlertDialogType
import com.aksoy.mybet.utils.IRecyclerClickListener
import com.aksoy.mybet.utils.SelectListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.rv_bet_odds_item.view.*
import org.greenrobot.eventbus.EventBus


class OddsAdapter(
    val list: List<OddsResponse>,private val cartLoadListener: ICartLoadListener) :
    RecyclerView.Adapter<OddsAdapter.OddsViewHolder>()
{
    class OddsViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    private var clickListener: IRecyclerClickListener? = null
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
                val gson = Gson()
                Log.i("message", "--> " + gson.toJson(e.message))

            }
    }
}