package com.aksoy.mybet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.OddModel
import com.aksoy.mybet.utils.IRecyclerClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.rv_cart_item.view.*


class CartAdapter (
    val context: Context, private val list: List<OddModel>,private val cartLoadListener: ICartLoadListener
): RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    class CartViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    private var clickListener: IRecyclerClickListener? = null

    fun setClickListener(clickListener: IRecyclerClickListener){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_cart_item, parent, false)
        return CartViewHolder(view)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.view.home_team.text = list[position].homeTeam
        holder.view.home_team_odd.text = list[position].homeTeamPrice.toString()
        holder.view.away_team.text = list[position].awayTeam
        holder.view.away_team_odd.text = list[position].awayTeamPrice.toString()
        holder.view.delete_the_odd.setOnClickListener{
            deleteData(list)
        }

    }

    private fun deleteData(list: List<OddModel>) {
        var mPostReference: DatabaseReference
        var cartUid =  0
        cartUid = cartUid.inc()
        mPostReference = FirebaseDatabase.getInstance().reference
            .child("Cart").child("1").child(cartUid.toString());
        mPostReference.removeValue()
    }


    override fun getItemCount(): Int {
        return list.size
    }

}