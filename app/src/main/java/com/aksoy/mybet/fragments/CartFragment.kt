package com.aksoy.mybet.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aksoy.mybet.R
import com.aksoy.mybet.adapters.CartAdapter
import com.aksoy.mybet.eventbus.UpdateCartEvent
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.listeners.IOddsListLisitener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.models.OddModel
import com.aksoy.mybet.utils.BaseFragment
import com.aksoy.mybet.viewmodels.CartViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.cart_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CartFragment : BaseFragment() , IOddsListLisitener, ICartLoadListener  {

    private lateinit var viewModel: CartViewModel
    private lateinit var oddListener: IOddsListLisitener
    private lateinit var cartLoadListener: ICartLoadListener
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        initView()
        loadBetFromFirebase()
        countCartFromFirebase()
    }



    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent)
    {
        countCartFromFirebase()
    }



    private fun countCartFromFirebase() {
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)

                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)

                }

            })
    }

    private fun loadBetFromFirebase() {
        startProgress()
        val betModelList: MutableList<OddModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Sports")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(betSnapshot in snapshot.children){
                            val betModel = betSnapshot.getValue(OddModel::class.java)
                            betModel!!.key = betSnapshot.key
                            betModelList.add(betModel)
                        }
                        oddListener.onBetLoadSuccess(betModelList)
                    }else {
                        dismissProgress()
                        rv_cart_list.visibility = View.GONE
                        empty_cart.visibility = View.VISIBLE
                        oddListener.onBetLoadFailed("ItemNotExist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    oddListener.onBetLoadFailed(error.message)
                }
            })

    }

    private fun initView() {
        oddListener = this
        cartLoadListener = this
    }
    override fun onBetLoadSuccess(betModelList: List<OddModel>) {
        dismissProgress()
        val adapter = CartAdapter(requireContext(), betModelList,cartLoadListener)
        rv_cart_list.adapter = adapter
    }

    override fun onBetLoadFailed(message: String) {
        Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG).show()

    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for(cartModel in cartModelList) cartSum += cartModel!!.quantity
//        badge!!.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(requireView(),message!!, Snackbar.LENGTH_LONG).show()
    }


}