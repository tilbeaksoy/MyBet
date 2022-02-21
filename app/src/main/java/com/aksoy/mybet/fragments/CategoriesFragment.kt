package com.aksoy.mybet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.adapters.SportsCategoriesAdapter
import com.aksoy.mybet.listeners.ICartLoadListener
import com.aksoy.mybet.models.CartModel
import com.aksoy.mybet.utils.BaseFragment
import com.aksoy.mybet.utils.OddsHelper
import com.aksoy.mybet.viewmodels.CategoriesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : BaseFragment() {

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var suplementAdapter: SportsCategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
        setObservers()
        viewModel.getSports()


    }



    private fun setObservers() {
        startProgress()
        viewModel.sportsResponse.observe(viewLifecycleOwner, { response ->
            if (!response.isNullOrEmpty()) {
                dismissProgress()
                suplementAdapter = SportsCategoriesAdapter(response)
                rv_bet_list.adapter = suplementAdapter
                suplementAdapter.sportsSelectedItem.observe(viewLifecycleOwner, { response ->
                    response?.let {
                        if (!response.sport_key.isNullOrEmpty() || !response.sport_tittle.isNullOrEmpty()) {
                            OddsHelper.setKeyNTittle(response.sport_key, response.sport_tittle!!)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.categoriesDetailFragment)
                        }

                    }
                })
            }
        })
    }


}