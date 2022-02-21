package com.aksoy.mybet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.eventbus.UpdateCartEvent
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.models.OddModel
import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.utils.IRecyclerClickListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.rv_cart_item.view.away_team
import kotlinx.android.synthetic.main.rv_cart_item.view.away_team_odd
import kotlinx.android.synthetic.main.rv_cart_item.view.home_team
import kotlinx.android.synthetic.main.rv_cart_item.view.home_team_odd
import org.greenrobot.eventbus.EventBus

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
        holder.view.home_team_odd.text = list[position].homeTeamOdd.toString()
        holder.view.away_team.text = list[position].awayTeam
        holder.view.away_team_odd.text = list[position].awayTeamOdd.toString()
        holder.view.setOnClickListener(object: IRecyclerClickListener,
            View.OnClickListener {
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(list[position])
            }

            override fun onClick(v: View?) {
                clickListener?.onItemClickListener(v,position)
            }

        })

    }
    private fun addToCart(oddsModel: OddModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
        userCart.child(oddsModel.key!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val cartModel = snapshot.getValue(CartModel::class.java)
                        val updateDate: MutableMap<String, Any> = HashMap()
                        cartModel?.quantity = cartModel!!.quantity + 1
                        updateDate["quantity"] = cartModel!!.quantity
                        updateDate["totalPrice"] = cartModel!!.quantity + cartModel.price.toFloat()
                        userCart.child(oddsModel.key!!)
                            .updateChildren(updateDate)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartLoadListener.onLoadCartFailed("Success add to cart")
                            }
                            .addOnFailureListener { e -> cartLoadListener.onLoadCartFailed(e.message)
                            }
                    }else {
                        val cartModel = CartModel()
                        cartModel.key = oddsModel.key
                        cartModel.awayTeam = oddsModel.awayTeam
                        cartModel.homeTeam = oddsModel.homeTeam
                        cartModel.homeTeamOdd = oddsModel.homeTeamOdd
                        cartModel.awayTeamOdd = oddsModel.awayTeamOdd
                        cartModel.price = oddsModel.price!!
                        cartModel.quantity = 1
                        cartModel.totalPrice = oddsModel.price!!.toFloat()
                        userCart.child(oddsModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartLoadListener.onLoadCartFailed("Success add to cart")
                            }
                            .addOnFailureListener { e-> cartLoadListener.onLoadCartFailed(e.message) }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    override fun getItemCount(): Int {
        return list.size
    }

}