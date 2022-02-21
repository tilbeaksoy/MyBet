package com.aksoy.mybet.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.aksoy.mybet.R
import com.aksoy.mybet.adapters.OddsAdapter
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.models.ModelForHelper
import com.aksoy.mybet.utils.BaseFragment
import com.aksoy.mybet.utils.OddsHelper
import com.aksoy.mybet.viewmodels.CategoriesDetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.categories_detail_fragment.*

class CategoriesDetailFragment : BaseFragment(), ICartLoadListener {


    private lateinit var viewModel: CategoriesDetailViewModel
    private var oddsModel = ModelForHelper()
    private lateinit var cartLoadListener: ICartLoadListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoriesDetailViewModel::class.java]
        setObservers()
        initView()
        oddsModel = OddsHelper.get()
        viewModel.getSportsOdds(oddsModel.sportsKey.toString())

    }
    private fun initView() {
        cartLoadListener = this
        img_back.setOnClickListener{
            activity?.onBackPressed()
        }
        img_close.setOnClickListener{
            Navigation.findNavController(requireView())
                .navigate(R.id.categoriesFragment)
        }

    }



    private fun setObservers() {
        startProgress()
        viewModel.sportOddsResponse.observe(viewLifecycleOwner, { response ->
            response?.let {
                dismissProgress()
                val  oddsAdapter = OddsAdapter(response)
                rv_odd_list.adapter = oddsAdapter
            }
        })
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for(cartModel in cartModelList) cartSum += cartModel!!.quantity
        badge!!.setNumber(cartSum)

    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(requireView(),message!!, Snackbar.LENGTH_LONG).show()

    }

}